package com.nichoshop.legacy.dao.slick

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.legacy.dao.ProductDAO
import com.nichoshop.legacy.models.ProductsRow
import com.nichoshop.legacy.slick.Tables._

import scala.slick.jdbc.StaticQuery.interpolation

class SlickProductDAO(val db: Database) extends ProductDAO {
  val rentCat = 21329
  val buyCat = 21330

  private def products(sellerId: Int, state: Byte, filter: Int) = {
    val q = Products.filter(p => p.sellerId === sellerId && p.state === state)

    (state, filter) match {
      case (x, 0) => q.sortBy(_.creationTime.desc)
      case (0, 1) => q.filter(_.fixedPrice).sortBy(_.creationTime.desc)
      case (0, 2) => q innerJoin Offers on ((p, o) => p.id === o.productId && !o.accepted) map (_._1) sortBy (_.creationTime.desc)
      case (0, 3) => q.filter(!_.fixedPrice).sortBy(_.creationTime.desc)
      case (0, 4) => q.filter(_.catId inSetBind List(rentCat, buyCat)).sortBy(_.creationTime.desc)
      case (1, 1) => q innerJoin Sells on ((p, s) => p.id === s.productId && !s.paid) map (_._1) sortBy (_.creationTime.desc)
      case (1, 2) => q innerJoin Sells on ((p, s) => p.id === s.productId && s.paid && !s.dispatched) map (_._1) sortBy (_.creationTime.desc)
      case (1, 3) => q innerJoin Sells on ((p, s) => p.id === s.productId && s.dispatched) map (_._1) sortBy (_.creationTime.desc)
      case (1, 4) => q innerJoin Sells on (_.id === _.productId) leftJoin Feedback on (_._2.id === _.sellId) filter (_._2.id.?.isEmpty) map (_._1._1) sortBy (_.creationTime.desc)
      case (3, 1) => q.sortBy(_.nowPrice.desc)
      case (3, 2) => q.sortBy(_.nowPrice.asc)
      case (3, 3) => q.sortBy(_.creationTime.desc) //newest
      case (3, 4) => q.sortBy(_.creationTime.asc) //oldest
      case(_,_) => q.sortBy(_.creationTime.desc)
    }
  }

  def watchlist(userId: Int, from: Int, size: Int): List[ProductsRow] = db.withSession { implicit session =>
    (Watchlist innerJoin Products on ((w, p) => w.userId === userId && w.productId === p.id)).map(_._2).drop(from).take(size).list
  }

  def deleteMarked(sellerId: Int, ids: List[Int]) = db.withSession { implicit session =>
//    Products.filter(p => p.sellerId === sellerId && p.id.inSetBind(ids)).delete
  }

  def findById(id: Int): Option[ProductsRow] = db.withSession { implicit session =>
    Products.filter(_.id === id).take(1).list.headOption
  }

  def findAll: List[ProductsRow] = db.withSession { implicit session =>
    Products.list
  }

  def create(x: ProductsRow) = db.withSession { implicit session =>
    Products += x
  }

  def deleteById(id: Int) = db.withSession { implicit session =>
//    Products.filter(_.id === id).delete
  }

  def updateById(id: Int, x: ProductsRow) = db.withSession { implicit session =>
    Products.filter(_.id === id).update(x.copy(id = id))
  }

  def filterByCategory(catId: Int): List[ProductsRow] = db.withSession { implicit session =>
    Products.filter(_.catId === catId).list
  }

  def filterByState(sellerId: Int, state: Byte, filter: Int, from: Int, size: Int): List[ProductsRow] = db.withSession { implicit session =>
    products(sellerId, state, filter).drop(from).take(size).list
  }

  def randomWithCategory(n: Int, catId: Int): List[ProductsRow] = db.withSession { implicit session =>
    //todo remove RAND()
    sql"SELECT id, seller_id, cat_id, title, subtitle, item_condition, images, fixed_price, starting_price, now_price, duration, location, creation_time, state, description, condition_desc, quantity, credit, paypal, bitcoin, gift, best_offer, returns, returns_details, domestic_service, domestic_cost, domestic_collect, international_service, international_cost, post_to FROM products WHERE cat_id = $catId ORDER BY RAND() LIMIT $n".as[ProductsRow].list
  }

  def search(s: String): List[ProductsRow] = db.withSession { implicit session =>
    //TODO create FULLTEXT index on title?
    Products.filter(_.title like s"%$s%").list
  }

  def searchWithCategoryId(catId: Int, str: String): List[ProductsRow] = db.withSession { implicit session =>
    Products.filter(p => p.catId === catId && (p.title like s"%$str%")).list
  }
}