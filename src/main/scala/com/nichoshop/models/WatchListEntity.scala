package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import scala.slick.lifted.Tag

/**
 * Created by Nursultan on 6/6/2022.
 */

case class WatchListEntity(

            override val id: Option[Int],
            userId: Int, 
            itemId: Int,
            created: Long = System.currentTimeMillis()

) extends IdAsPrimaryKey

class WatchLists(tag:Tag) extends TableWithIdAsPrimaryKey[WatchListEntity](tag, "watch_lists"){

    def userId = column[Int]("user_id")
    def itemId = column[Int]("item_id")
    def created = column[Long]("created")

    def * = (id.?, userId, itemId, created) <> (WatchListEntity.tupled, WatchListEntity.unapply)
}

object WatchLists extends QueryForTableWithIdAsPrimaryKey[WatchListEntity, WatchLists](TableQuery[WatchLists]) {

}