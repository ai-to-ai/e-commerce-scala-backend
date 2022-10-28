package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import scala.slick.lifted.Tag

/**
 * Created by NurSultan on 6/7/2022
 */
case class BidEntity(

            override val id: Option[Int] = None,
            itemId: Int,
            userId: Int,
            created: Long = System.currentTimeMillis(),
            bid: RichMoney

) extends IdAsPrimaryKey

class Bids(tag:Tag) extends TableWithIdAsPrimaryKey[BidEntity](tag, "bids") {
  
  def itemId = column[Int]("item_id")
  def userId = column[Int]("user_id")
  def created = column[Long]("created")

  def bidAmount = column[Int]("amount")
  def bidCurrencyId = column[Int]("currency_id")
  def bid = (bidAmount, bidCurrencyId) <> (RichMoney.tupled, RichMoney.unapply)

  def * = ( id.?, itemId, userId, created, bid) <> ( BidEntity.tupled, BidEntity.unapply )
}

object Bids extends QueryForTableWithIdAsPrimaryKey[BidEntity, Bids](TableQuery[Bids]) {

}
