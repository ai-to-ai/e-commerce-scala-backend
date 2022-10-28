package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers.{DB, RichMoney}
import com.nichoshop.model.dto._
import com.nichoshop.models.{ItemEntity, Items, WatchLists, FeedBacks, Sells, Variants, VariantEntity, Categories, ItemViews}
import com.nichoshop.utils.{Pagination, Converters}
import scala.util.control.NonFatal
import com.nichoshop.utils.Logger

/**
 * Created by Nursultan on 6/7/2022
 */

class ItemService extends Logger {

  import ItemState._
  import Converters._
  val dailyDealsId = 21324

  val rentCat = 21329
  val buyCat = 21330

  val items = Items.query
  val watchLists = WatchLists.query
  val feedBacks = FeedBacks.query
  val sells = Sells.query
  val variants = Variants.query
  val itemViews = ItemViews.query


  private def itemsFilter(sellerId: Int, state: Int, filter: Int) = {
    val q = items.filter(p => p.sellerId === sellerId && p.state === state)

    (state, filter) match {
      case (x, 0) => q.sortBy(_.created.desc)
      case (0, 1) => q.filter(_.listingFormat === 0).sortBy(_.created.desc)
    //   case (0, 2) => q innerJoin Offers on ((p, o) => p.id === o.itemId && !o.accepted) map (_._1) sortBy (_.created.desc)
      case (0, 3) => q.filter(_.listingFormat === 1).sortBy(_.created.desc)
      case (0, 4) => q.filter(_.catId inSetBind List(rentCat, buyCat)).sortBy(_.created.desc)
      case (1, 1) => q innerJoin sells on ((p, s) => p.id === s.itemId && !s.paid) map (_._1) sortBy (_.created.desc)
      case (1, 2) => q innerJoin sells on ((p, s) => p.id === s.itemId && s.paid && !s.dispatched) map (_._1) sortBy (_.created.desc)
      case (1, 3) => q innerJoin sells on ((p, s) => p.id === s.itemId && s.dispatched) map (_._1) sortBy (_.created.desc)
      case (1, 4) => q innerJoin sells on (_.id === _.itemId) leftJoin feedBacks on (_._2.id === _.sellId) filter (_._2.id.?.isEmpty) map (_._1._1) sortBy (_.created.desc)
      case (3, 1) => q.sortBy(_.nowPrice.desc)
      case (3, 2) => q.sortBy(_.nowPrice.asc)
      case (3, 3) => q.sortBy(_.created.desc) //newest
      case (3, 4) => q.sortBy(_.created.asc) //oldest
      case(_,_) => q.sortBy(_.created.desc)
    }
  }

  def watchlist_(userId: Int, from: Int, size: Int): List[ItemEntity] = DB.read { implicit session =>
    (watchLists innerJoin items on ((w, p) => w.userId === userId && w.itemId === p.id)).map(_._2).drop(from).take(size).list
  }

  def deleteMarked(sellerId: Int, ids: List[Int]) = DB.write
   { implicit session =>
//    items.filter(p => p.sellerId === sellerId && p.id.inSetBind(ids)).delete
  }

  def findById(id: Int): Option[ItemEntity] = DB.read { implicit session =>
    items.filter(_.id === id).take(1).list.headOption
  }

  def findAll: List[ItemEntity] = DB.read { implicit session =>
    items.list
  }

  def deleteById(id: Int) = DB.write { implicit session =>
//    items.filter(_.id === id).delete
  }

  def updateById(id: Int, x: ItemEntity) = DB.write { implicit session =>
    items.filter(_.id === id).update(x.copy(id = Option(id)))
  }

  def filterByCategory(catId: Int): List[ItemEntity] = DB.read { implicit session =>
    items.filter(_.catId === catId).list
  }

  def filterByState_(sellerId: Int, state: Byte, filter: Int, from: Int, size: Int): List[ItemEntity] = DB.read { implicit session =>
    itemsFilter(sellerId, state, filter).drop(from).take(size).list
  }

  // def randomWithCategory(n: Int, catId: Int): List[ItemEntity] = DB.read { implicit session =>
    //todo remove RAND()
    // sql"SELECT id, seller_id, cat_id, title, subtitle, item_condition, images, fixed_price, starting_price, now_price, duration, location, creation_time, state, description, condition_desc, quantity, credit, paypal, bitcoin, gift, best_offer, returns, returns_details, domestic_service, domestic_cost, domestic_collect, international_service, international_cost, post_to FROM items WHERE cat_id = $catId ORDER BY RAND() LIMIT $n".as[ItemEntity].list
  // }

  def search(s: String): List[ItemEntity] = DB.read { implicit session =>
    //TODO create FULLTEXT index on title?
    items.filter(_.title like s"%$s%").list
  }

  def searchforCategoryList(s: String): List[Int] = DB.read { implicit session =>
    
    //TODO search items for category list
    val cList = items.filter(_.title like s"%$s%").list.map(_.catId).take(10)
    log.info(s"$cList")
    cList
  }

  def searchWithCategoryId(catId: Int, str: String): List[ItemEntity] = DB.read { implicit session =>
    items.filter(p => p.catId === catId && (p.title like s"%$str%")).list
  }


  // def bestitems(): List[ItemEntity] = {
    // randomWithCategory(12, dailyDealsId)
  // }

  def watchlist(userId: Int)(implicit paging: (Int, Int)): List[ItemEntity] = {
    watchlist_(userId, (paging._2 - 1) * paging._1, paging._1)
  }

  def active(sellerId: Int, filter: Int)(implicit paging: (Int, Int)): List[ItemEntity] = {
    filterByState_(sellerId, ACTIVE, filter, (paging._2 - 1) * paging._1, paging._1)
  }

  def sold(sellerId: Int, filter: Int)(implicit paging: (Int, Int)): List[ItemEntity] = {
    filterByState_(sellerId, SOLD, filter, (paging._2 - 1) * paging._1, paging._1)
  }

  def unsold(sellerId: Int)(implicit paging: (Int, Int)): List[ItemEntity] = {
    filterByState_(sellerId, UNSOLD, 0, (paging._2 - 1) * paging._1, paging._1)
  }

  def archive(sellerId: Int, filter: Int)(implicit paging: (Int, Int)): List[ItemEntity] = {
    filterByState_(sellerId, ARCHIVED, filter, (paging._2 - 1) * paging._1, paging._1)
  }

  def getRecentItems(userId: Int) : List[ItemEntity] = DB.read { implicit session =>
    val idsList = itemViews.filter(_.userId === userId)
                            .sortBy(_.created.desc)
                            .take(10).map(_.itemId).list
    
    items.filter(_.sellerId === userId)
          .filter(i => i.id inSetBind idsList )
          .list
    
  }
  
  def create(x: ItemEntity): Unit = DB.write { implicit session =>
      items.insert(x)  
  }

  def getItem(itemId: Int): Option[ItemDto] = {

    DB.read { implicit session =>
      (for {
        item <- items
        if item.id === itemId
      } yield item).firstOption.map {item =>
        val variantsList = (for {
          variant <- variants
          if variant.itemId === item.id
        } yield variant).list

        (item, variantsList): ItemDto
      }
    }
  }

  def getItems(categoryId: Int): List[ItemDto] = {

    DB.read { implicit session =>
      val itemsList =
        (for {
          item <- items
          if item.catId === categoryId
        } yield item).list
      itemsList.map { item =>
        val variantsList = (for {
          variant <- variants
          if variant.itemId === item.id
        } yield variant).list

        (item, variantsList): ItemDto
      }
    }
  }

  // def createItem(item: ItemDto):Boolean = {

  //   try {
  //     DB.write { implicit session =>
  //       val p = ItemEntity(
  //                   None, 
  //                   item.getSellerId, 
  //                   item.getCategoryId, 
  //                   item.getTitle, item.getDescription, item.getCreated)
  //       val itemId = items.insert(p)
  //       item.getVariants.foreach {variant =>
  //         val pv = VariantEntity(None,
  //           itemId = itemId,
  //           Some(variant.getTitle),
  //           Some(variant.getDescription),
  //           variant.getCondition,
  //           RichMoney(variant.getPrice.getAmount, variant.getPrice.getCurrencyId), variant.getAmount, variant.getCreated)
  //         variants.insert(pv)
  //       }
  //       true
  //     }
  //   } catch {
  //     case NonFatal(e) =>
  //       log.error("Exception while creating item -->", e)
  //       false
  //   }

  // }
  def createVariant(itemId: Int, variant: VariantDto):Boolean = {
    try {
      DB.write { implicit session =>
          val v = VariantEntity(None,
            itemId,
            Option(variant.getTitle),
            Option(variant.getDescription),
            variant.getCondition,
            RichMoney(variant.getPrice.getAmount, variant.getPrice.getCurrencyId), variant.getAmount, variant.getCreated)
          variants.insert(v)

        true
      }
    } catch {
      case NonFatal(e) =>
        log.error("Exception while creating item -->", e)
        false
    }
  }
  def deleteVariant(itemId: Int, variantId: Int):Boolean = {
    try {
      DB.write { implicit session =>
        variants.filter(_.id === variantId).delete

        true
      }
    } catch {
      case NonFatal(e) =>
        log.error("Exception while creating item -->", e)
        false
    }
  }

  // def getUserProducts(userId: Int, categoryId: Option[Int], c: String) = {
  //   val condition: Option[String] = Try(ProductCondition.valueOf(c)).toOption.map( v => v : String)

  //     DB.read { implicit session =>
  //       val itemsList =
  //         (for {
  //           item <- items
  //           if item.sellerId === userId
  //         } yield item).list

  //       val categories = Categories.getCategoriesWithParentsOrdered(itemsList.map(_.catId))

  //       itemsList.flatMap { item =>
  //         val variants = {
  //           val q = for {
  //             variant <- variants
  //             if variant.itemId === item.id
  //           } yield variant

  //           log.info(s"condition to select ${condition}")

  //           condition.fold(q)(v => q.filter(_.condition === v))
  //         }.list

  //         if (variants.isEmpty) None
  //         else Some((item, variants): ItemDto)
  //       }
  //     }
  // }

}

object ItemState {
  val ACTIVE: Byte = 0
  val SOLD: Byte = 1
  val UNSOLD: Byte = 2
  val ARCHIVED: Byte = 3
}
