package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 6/8/2022
 */

case class OrderEntity(

            override val id: Option[Int] = None,
            buyerId: Int,
            buyerName: String = "",
            sellerName: String = "",
            sellerId: Int,
            payment: Int,
            delivery: Long,
            trackingId: Int,
            orderId: String = "",
            items: List[String] = List(),
            itemId: Int = 0,
            status: Int = 0,
            soldBySeller: Int = 1,
            total: RichMoney,
            cancel: Boolean = false,
            cancelDate: Option[Long] = None,
            soldDate: Option[Long] = None,
            shipByDate: Option[Long] = None,
            created: Long = System.currentTimeMillis()

) extends IdAsPrimaryKey

class Orders(tag:Tag) extends TableWithIdAsPrimaryKey[OrderEntity](tag, "orders") {
implicit val commaSeparatedString = separatedString()

  def buyerId = column[Int]("buyer_id")
  def buyerName = column[String]("buyer_name")
  def sellerName = column[String]("seller_name")
  def sellerId = column[Int]("seller_id")
  def payment = column[Int]("payment")
  def delivery = column[Long]("delivery")
  def trackingId = column[Int]("tracking_id")
  def orderId = column[String]("order_id")
  def items = column[List[String]]("items")
  def itemId = column[Int]("item_id")
  def status = column[Int]("status")
  def soldBySeller = column[Int]("sold_by_seller")
  def totalCurrency = column[Int]("total_currency")
  def totalAmount = column[Int]("total_amount")
  def cancel = column[Boolean]("cancel")
  def cancelDate = column[Option[Long]]("cancel_date")
  def soldDate = column[Option[Long]]("sold_date")
  def shipByDate = column[Option[Long]]("ship_by_date")
  def created = column[Long]("created")

  def total = (totalAmount, totalCurrency) <> (RichMoney.tupled, RichMoney.unapply)

  def * = (id.?, buyerId, buyerName, sellerName, sellerId, payment, delivery, trackingId, orderId, items, itemId, status, soldBySeller,total, cancel, cancelDate, soldDate, shipByDate, created) <> (OrderEntity.tupled, OrderEntity.unapply)
}

object Orders extends QueryForTableWithIdAsPrimaryKey[OrderEntity, Orders](TableQuery[Orders]) {
    object Status {
        val delivered = 1
        val shipped = 2
        val notShipped = 3
        val feedback = 4
    }
}