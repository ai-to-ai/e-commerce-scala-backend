package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.model.dto._
import com.nichoshop.models.{Categories,Conditions, Specifics, CategoryEntity, SpecificEntity}
import com.nichoshop.utils.Marshallers
import com.nichoshop.models.helpers.DB
import com.nichoshop.utils.Conversions
import scala.util.control.Breaks._
import org.slf4j.LoggerFactory
import com.nichoshop.utils.CaseClasses._

class CategoryService {

  private val log = LoggerFactory.getLogger(getClass)

  private val categories = Categories.query
  private val conditions = Conditions.query
  private val specifics = Specifics.query

  def getList(): List[CategoryEntity] = DB.read { implicit session =>
      categories.list  
  }

  def subcategories(id: Int): List[CategoryEntity] =Categories.findByParentId(id)

  def sidebarCategories: List[CategoryEntity] =
    Categories.sidebarCategories.sortBy(_._1.ord)
              .map {case (parent, children) => parent -> children.sortBy(_.ord)}
              .map {case (parent, children) => parent::children}.flatten

  def sidebarCategories2 = Conversions.toCategoryTree(Categories.sidebarCategories)

  def categoriesTree = Conversions.toCategoryTree(DB.read{ implicit s =>categories.list }, 0).get

  def categoryList(parentId: Int) =
    Conversions.toCategoryTree(DB.read{ implicit s =>Categories.findById(parentId).toList ++Categories.findByParentId(parentId)  }.map {
      case category if category.conditions.isEmpty =>
        val cats =Categories.getCategoriesWithParents( List(category.id.get) ).map(c => c.id.get -> c).toMap

        def getConditions(id: Int): List[String] = {
          if (id == 0) Nil
          else {
            val next = cats(id)
            if (!next.conditions.isEmpty) next.conditions
            else getConditions(next.parentId)
          }
        }
        category.copy(conditions = getConditions(category.id.get))
      case category => category
    }, parentId).get

  def createCategory(name: String, parentId: Int = 0) = DB.write  {implicit session =>
    if( parentId > 0){
      if(categories.filter(_.id === parentId).list.length == 0) Marshallers.bad("Cannot find parent category.")
    }
    
    Categories.insert(CategoryEntity(None, name, leaf = true, parentId,Categories.nextOrd(parentId)))

    Categories.flushCache()
  }

  def updateCategory(categoryId: Int, conditions: List[String] = Nil, specifics: Option[SpecificArrayDto]) = DB.write { implicit session =>
    val updated =categories.filter(_.id === categoryId)
                            .map(c =>(c.conditions, c.specifics))
                            .update(conditions,specifics)
    Categories.flushCache()
    updated
  }

  def moveCategory(parentId: Int, childId: Int) = DB.write  {implicit session =>
    categories.filter(_.id === childId)
              .map(_.parentId)
              .update(parentId)

    Categories.flushCache()
  }

  def removeCategory(categoryId: Int) = DB.write { implicit session =>
    categories.filter(_.parentId === categoryId)
              .delete 
    categories.filter(_.id === categoryId)
              .delete
              
    Categories.flushCache()
  }

  def renameCategory(categoryId:Int, name: String) = DB.write { implicit session =>
    categories.filter(_.id === categoryId)
              .map(_.name)
              .update(name)

    Categories.flushCache()
  }

  def getCategoryList(keyword: String) = DB.read { implicit session =>
    val searchList =categories.filter(_.name like s"%${keyword}%").list.map(_.id.get)

    Categories.getCategoriesWithParents(searchList)
  }

  def getCategoryfromList(searchList: List[Int]) = DB.read { implicit session =>
    Conversions.toCategoryTree(
      Categories.getCategoriesWithParents(searchList),0
    ).get
  }

  
  /**
   * Functions for Conditions and Specifics
   */

  def getConditionsList() = DB.read { implicit session =>
    conditions.list
  }

  def getSpecificsList() = DB.read { implicit session =>
    specifics.list
  }

  def createSpecific(n : String) = DB.write { implicit session =>
      Specifics.insert(new SpecificEntity(name = n))
  }

}
