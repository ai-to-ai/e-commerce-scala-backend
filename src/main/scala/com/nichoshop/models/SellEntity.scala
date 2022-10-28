package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import scala.slick.lifted.Tag

/**
 * Created by Nursultan on 6/9/2022.
 */


case class SellEntity(
            override val id: Option[Int], 
            buyerId: Int, 
            itemId: Int, 
            quantity: Int, 
            price: Int, 
            paid: Boolean, 
            dispatched: Boolean, 
            soldTime: Long, 
            sellerId: Int,
            created: Long = System.currentTimeMillis()
) extends IdAsPrimaryKey

class Sells(tag:Tag) extends TableWithIdAsPrimaryKey[SellEntity](tag, "sells"){

    def buyerId = column[Int]("buyer_id")
    def itemId = column[Int]("item_id")
    def quantity = column[Int]("quantity")
    def price = column[Int]("price")
    def paid = column[Boolean]("paid")
    def dispatched = column[Boolean]("dispatched")
    def soldTime = column[Long]("soldTime")
    def sellerId = column[Int]("sellerId")
    def created = column[Long]("created")

    def * = (id.?, buyerId, itemId,quantity, price, paid, dispatched, soldTime, sellerId, created) <> (SellEntity.tupled, SellEntity.unapply)
}

object Sells extends QueryForTableWithIdAsPrimaryKey[SellEntity, Sells](TableQuery[Sells]) {

}
