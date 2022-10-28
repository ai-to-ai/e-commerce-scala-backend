package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 6/30/2022
 */

case class ConditionEntity(

            override val id: Option[Int] = None,
            name: String

) extends IdAsPrimaryKey

class Conditions(tag:Tag) extends TableWithIdAsPrimaryKey[ConditionEntity](tag, "conditions") {

  def name = column[String]("name")

  def * = (id.?, name) <> (ConditionEntity.tupled, ConditionEntity.unapply)
}

object Conditions extends QueryForTableWithIdAsPrimaryKey[ConditionEntity, Conditions](TableQuery[Conditions]) {
}