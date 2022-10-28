package com.nichoshop.legacy.dao.slick

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.legacy.dao.CategoryDAO
import com.nichoshop.legacy.models.CategoriesRow
import com.nichoshop.legacy.slick.Tables._

class SlickCategoryDAO(val db: Database) extends CategoryDAO {

  def findById(id: Int): Option[CategoriesRow] = db.withSession { implicit session =>
    Categories.filter(_.id === id).list.headOption
  }

  def findAll: List[CategoriesRow] = db.withSession { implicit session =>
    Categories.list
  }

  def create(x: CategoriesRow) = db.withSession { implicit session =>
    Categories += x
  }

  def deleteById(id: Int) = db.withSession { implicit session =>
//    Categories.filter(_.id === id).delete
  }

  def updateById(id: Int, x: CategoriesRow) = db.withSession { implicit session =>
    Categories.filter(_.id === id).update(x.copy(id = id))
  }

  def subcategories(id: Int): List[CategoriesRow] = db.withSession { implicit session =>
    Categories.filter(_.parentId === id).list
  }

  def sidebarCategories: List[CategoriesRow] = db.withSession { implicit session =>
    Categories.filter(_.parentId in Categories.filter(_.parentId === 0).map(_.id)) union
      Categories.filter(_.parentId === 0) sortBy (_.ord) sortBy (_.parentId) list
  }


}
