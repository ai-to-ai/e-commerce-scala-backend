package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 6/8/2022
 */

case class TrackingEntity(

            override val id: Option[Int] = None,
            userId: Int, 
            carrier: String,
            number: String,
            step: Int,
            created: Long = System.currentTimeMillis()

) extends IdAsPrimaryKey

class Trackings(tag:Tag) extends TableWithIdAsPrimaryKey[TrackingEntity](tag, "trackings") {

  def userId = column[Int]("user_id")
  def carrier = column[String]("carrier")
  def number = column[String]("number")
  def step = column[Int]("step")
  def created = column[Long]("created")

  def * = (id.?, userId, carrier, number, step, created) <> (TrackingEntity.tupled, TrackingEntity.unapply)
}

object Trackings extends QueryForTableWithIdAsPrimaryKey[TrackingEntity, Trackings](TableQuery[Trackings]) {
    object Status {
        val notShipped = 0
        val shipped = 1
        val delivered = 2
        val feedback = 3
    }
}