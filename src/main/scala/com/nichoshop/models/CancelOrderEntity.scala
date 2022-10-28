package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 6/7/2022
 */

 case class CancelOrderEntity(

            override val id: Option[Int] = None,
            userId: Int,
            orderId: Int,
            reasonId: Int,
            comment: Option[String] = None,
            items: List[String] = List(),
            qtys: List[String] = List(),
            accepted: Boolean = false,
            created: Long = System.currentTimeMillis()


 ) extends IdAsPrimaryKey

class CancelOrders(tag:Tag) extends TableWithIdAsPrimaryKey[CancelOrderEntity](tag, "cancel_orders") {
    implicit val commaSeparatedString = separatedString()

    def userId = column[Int]("user_id")
    def orderId = column[Int]("order_id")
    def reasonId = column[Int]("reason_id")
    def comment = column[Option[String]]("comment")
    def items = column[List[String]]("items")
    def qtys = column[List[String]]("qtys")
    def accepted = column[Boolean]("accepted")
    def created = column[Long]("created")

    def * = (id.?, userId, orderId, reasonId, comment, items, qtys, accepted, created) <> (CancelOrderEntity.tupled, CancelOrderEntity.unapply)

}

object CancelOrders extends QueryForTableWithIdAsPrimaryKey[CancelOrderEntity, CancelOrders](TableQuery[CancelOrders]){
    
}