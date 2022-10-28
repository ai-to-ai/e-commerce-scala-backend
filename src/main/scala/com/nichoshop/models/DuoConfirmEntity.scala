package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import scala.slick.lifted.Tag

/**
 * Created by Nursultan on 6/16/2022
 */
case class DuoConfirmEntity(

            override val id: Option[Int] = None,
            userName: String,
            state: String,
            created: Long = System.currentTimeMillis()
            
) extends IdAsPrimaryKey

class DuoConfirms(tag:Tag) extends TableWithIdAsPrimaryKey[DuoConfirmEntity](tag, "duo_confirms") {
  
  def userName = column[String]("username")
  def state = column[String]("state")
  def created = column[Long]("created")

  def * = ( id.?, userName, state, created) <> ( DuoConfirmEntity.tupled, DuoConfirmEntity.unapply )

}

object DuoConfirms extends QueryForTableWithIdAsPrimaryKey[DuoConfirmEntity, DuoConfirms](TableQuery[DuoConfirms]) {

}