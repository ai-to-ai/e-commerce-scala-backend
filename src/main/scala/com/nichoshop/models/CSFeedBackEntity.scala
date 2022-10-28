package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 6/30/2022
 */

case class CSFeedBackEntity(

            override val id: Option[Int] = None,
            name: String

) extends IdAsPrimaryKey

class CSFeedBacks(tag:Tag) extends TableWithIdAsPrimaryKey[CSFeedBackEntity](tag, "custommer_support_feedbacks") {

  def name = column[String]("name")

  def * = (id.?, name) <> (CSFeedBackEntity.tupled, CSFeedBackEntity.unapply)
}

object CSFeedBacks extends QueryForTableWithIdAsPrimaryKey[CSFeedBackEntity, CSFeedBacks](TableQuery[CSFeedBacks]) {
}