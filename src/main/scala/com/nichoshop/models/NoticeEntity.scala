package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import scala.slick.lifted.Tag

/**
 * Created by Nurusultan on 5/20/2022.
 */
case class NoticeEntity(

            override val id: Option[Int] = None,
            from: Int,
            to: Int,
            noticeType: Int,
            cancelOrderId: Option[Int] = None,
            created: Long = System.currentTimeMillis()
            
) extends IdAsPrimaryKey

class Notices(tag:Tag) extends TableWithIdAsPrimaryKey[NoticeEntity](tag, "notices") {

    def from = column[Int]("from")
    def to = column[Int]("to")
    def noticeType = column[Int]("notice_type")
    def cancelOrderId = column [Option[Int]]("cancel_order_id")
    def created = column[Long]("created")

    def * = ( id.?, from, to, noticeType, cancelOrderId, created) <> ( NoticeEntity.tupled, NoticeEntity.unapply )
}

object Notices extends QueryForTableWithIdAsPrimaryKey[NoticeEntity, Notices](TableQuery[Notices]) {

}
