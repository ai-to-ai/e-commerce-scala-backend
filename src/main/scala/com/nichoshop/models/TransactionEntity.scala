package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 6/8/2022
 */

case class TransactionEntity(

            override val id: Option[Int] = None,
            status: Int, 
            buyerId: Int,
            sellerId: Int,
            created: Long = System.currentTimeMillis()

) extends IdAsPrimaryKey

class Transactions(tag:Tag) extends TableWithIdAsPrimaryKey[TransactionEntity](tag, "transactios") {

  def status = column[Int]("status")
  def buyerId = column[Int]("buyer_id")
  def sellerId = column[Int]("seller_id")
  def created = column[Long]("created")

  def * = (id.?, status, buyerId, sellerId, created) <> (TransactionEntity.tupled, TransactionEntity.unapply)
}

object Transactions extends QueryForTableWithIdAsPrimaryKey[TransactionEntity, Transactions](TableQuery[Transactions]) {
}