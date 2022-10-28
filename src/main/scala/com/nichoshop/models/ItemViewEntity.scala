package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import scala.slick.lifted.Tag

/**
 * Created by Nursultan on 6/14/2022
 */
case class ItemViewEntity(

            override val id: Option[Int],
            userId: Int,
            itemId: Int,
            created: Long = System.currentTimeMillis(),
            
) extends IdAsPrimaryKey

class ItemViews (tag:Tag) extends TableWithIdAsPrimaryKey[ItemViewEntity](tag, "item_views") {
  def userId = column[Int]("user_id")
  def itemId = column[Int]("item_id")
  def created = column[Long]("created")

  def * = ( id.?, userId, itemId, created) <> ( ItemViewEntity.tupled, ItemViewEntity.unapply )

}

object ItemViews extends QueryForTableWithIdAsPrimaryKey[ItemViewEntity, ItemViews](TableQuery[ItemViews]) {

}