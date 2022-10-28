package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._
import com.nichoshop.model.dto._

import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import scala.concurrent.Future
import scala.slick.lifted.Tag

/**
 * Created by Evgeny Zhoga on 27.06.15.
 */
case class CategoryEntity
(
            override val id: Option[Int],
            name: String,
            leaf: Boolean,
            parentId: Int = 0,
            ord: Int = 0,
            conditions: List[String] = List(),
            specifics: Option[SpecificArrayDto] = None
        
) extends IdAsPrimaryKey
object SpecificImplicits {
  implicit val schema = SpecificArrayDto.getClassSchema
  implicit val avroMapper = avroBasedColumnType[SpecificArrayDto]
  implicit val commaSeparatedString = separatedString()
}

class Categories(tag:Tag) extends TableWithIdAsPrimaryKey[CategoryEntity](tag, "categories") {
  import SpecificImplicits._

  def name = column[String]("name")
  def leaf = column[Boolean]("leaf")
  def parentId = column[Int]("parent_id")
  def ord = column[Int]("ord")
  def conditions = column[List[String]]("conditions")
  def specifics = column[Option[SpecificArrayDto]]("specifics")

  def * = ( id.?, name, leaf, parentId, ord, conditions, specifics) <> ( CategoryEntity.tupled, CategoryEntity.unapply )
}

object Categories extends QueryForTableWithIdAsPrimaryKey[CategoryEntity, Categories](TableQuery[Categories]) {
  private case class CategoriesCache(timestamp: Long, idWithParentId: List[Tuple2[Int, Int]]) {
    val asMap = idWithParentId.toMap
  }

  private val categoriesCache = new AtomicReference[CategoriesCache]

  def findByParentId(parentId: Int): List[CategoryEntity] = DB.read { implicit session =>
    query.filter(_.parentId === parentId).list
  }
  private def getCategoriesCache = DB.read { implicit session =>
    Option(categoriesCache.get()).
      fold {
      val cc = CategoriesCache(System.currentTimeMillis(), query.map(c => c.id -> c.parentId).list)
      categoriesCache.set(cc)
      cc
    } {cc =>
      if (cc.timestamp + TimeUnit.HOURS.toMillis(3) < System.currentTimeMillis()) {
        val cc = CategoriesCache(System.currentTimeMillis(), query.map(c => c.id -> c.parentId).list)
        categoriesCache.set(cc)
        cc
      } else {
        cc
      }
    }
  }

  def flushCache(): Unit = {
    import scala.concurrent.ExecutionContext.Implicits.global

    categoriesCache.set(null)

    Future {
      getCategoriesCache
    }
  }

  def getCategoriesWithParents(categoryIds: List[Int]) = {
    val cc = getCategoriesCache.asMap
    val categories = categoryIds.flatMap {catId =>
      def tail(parentId: Int): List[Int] = {
        if (parentId == 0) Nil
        else parentId :: tail(cc(parentId))
      }
      tail(catId)
    }.toSet
    DB.read { implicit session =>
      query.filter(c => c.id inSet categories).list
    }
    
  }

  def getCategoriesWithParentsOrdered(categoryIds: List[Int]) = {
    val cc: Map[Int, com.nichoshop.models.CategoryEntity] = getCategoriesWithParents(categoryIds).map(c => c.id.get -> c).toMap

    def tail(parent: Option[com.nichoshop.models.CategoryEntity]): List[com.nichoshop.models.CategoryEntity] = {
      if (parent.isEmpty) Nil
      else parent.get :: tail(cc.get(parent.get.parentId))
    }

    categoryIds.map { id =>
      id -> tail(cc.get(id))
    }.toMap
  }

  def sidebarCategories: List[(CategoryEntity, List[CategoryEntity])] = DB.read { implicit session =>
    val firstLevel = findByParentId(0)

    val secondLevel = (for {
      category <- Categories.query
      if category.parentId inSet firstLevel.map(_.id.get).toSet
    } yield category).list.groupBy(_.parentId)

    firstLevel.map(c1 => c1 -> secondLevel.getOrElse(c1.id.get, List.empty))
  }

  def nextOrd(parentId: Int = 0) = DB.read { implicit session =>
    query.filter( _.parentId === parentId ).map(_.ord).run.headOption.map(_ + 1).getOrElse(1)
  }

}