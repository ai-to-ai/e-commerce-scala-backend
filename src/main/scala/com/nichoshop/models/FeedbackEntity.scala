package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 6/8/2022
 */

case class FeedBackEntity(

            override val id: Option[Int] = None, 
              sellerId: Int, 
              sellId: Int, 
              rating: Int,
              userId: Int, 
              itemAsDescribed: Int, 
              communication: Int, 
              shippingTime: Int, 
              shippingCharges: Int, 
              message: Option[String] = None, 
              created: Long = System.currentTimeMillis
) extends IdAsPrimaryKey

class FeedBacks(tag:Tag) extends TableWithIdAsPrimaryKey[FeedBackEntity](tag, "feedbacks") {

  def sellerId = column[Int]("seller_id")
  def sellId = column[Int]("sell_id")
  def userId = column[Int]("user_id")
  def rating = column[Int]("rating")
  def itemAsDescribed = column[Int]("item_as_described")
  def communication = column[Int]("communication")
  def shippingTime = column[Int]("shipping_time")
  def shippingCarges = column[Int]("shipping_charges")
  def message = column[Option[String]]("message")
  def created = column[Long]("created")

  def * = (id.?, sellerId, sellId, userId, rating, itemAsDescribed, communication, shippingTime, shippingCarges, message, created) <> (FeedBackEntity.tupled, FeedBackEntity.unapply)
}

object FeedBacks extends QueryForTableWithIdAsPrimaryKey[FeedBackEntity, FeedBacks](TableQuery[FeedBacks]) {
}