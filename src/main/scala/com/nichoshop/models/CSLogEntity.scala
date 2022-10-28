package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 7/8/2022
 */

case class CSLogEntity(

            override val id: Option[Int] = None,
            csId: Int,
            from: Long,
            to : Option[Long]

) extends IdAsPrimaryKey

class CSLogs(tag:Tag) extends TableWithIdAsPrimaryKey[CSLogEntity](tag, "customer_support_logs") {

  def csId = column[Int]("cs_id")
  def from = column[Long]("from")
  def to = column[Option[Long]]("to")

  def * = (id.?, csId, from, to) <> (CSLogEntity.tupled, CSLogEntity.unapply)
}

object CSLogs extends QueryForTableWithIdAsPrimaryKey[CSLogEntity, CSLogs](TableQuery[CSLogs]) {
}