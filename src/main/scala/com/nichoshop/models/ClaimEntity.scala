package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 6/24/2022
 */

case class ClaimEntity(

            override val id: Option[Int] = None, 
              orderId: Int,
              itemIds: List[String] = List(),
              qtys: List[String] = List(),
              reason: Option[String] = None,
              status: Int = 0,
              created: Long = System.currentTimeMillis

) extends IdAsPrimaryKey

class Claims(tag:Tag) extends TableWithIdAsPrimaryKey[ClaimEntity](tag, "claims") {
  implicit val commaSeparatedString = separatedString()

  def orderId = column[Int]("order_id")
  def itemIds = column[List[String]]("item_ids")
  def qtys = column[List[String]]("qtys")
  def reason = column[Option[String]]("reason")
  def status = column[Int]("status")
  def created = column[Long]("created")

  def * = (id.?, orderId, itemIds, qtys, reason, status, created) <> (ClaimEntity.tupled, ClaimEntity.unapply)
}

object Claims extends QueryForTableWithIdAsPrimaryKey[ClaimEntity, Claims](TableQuery[Claims]) {
}