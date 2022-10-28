package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 7/3/2022
 */

case class EmailConfirmEntity(

            override val id: Option[Int] = None,
            userId: Int,
            code: String,
            created: Long = System.currentTimeMillis()

) extends IdAsPrimaryKey

class EmailConfirms(tag:Tag) extends TableWithIdAsPrimaryKey[EmailConfirmEntity](tag, "email_confirms") {

  def userId = column[Int]("user_id")
  def code = column[String]("code")
  def created = column[Long]("created")

  def * = (id.?, userId, code, created) <> (EmailConfirmEntity.tupled, EmailConfirmEntity.unapply)
}

object EmailConfirms extends QueryForTableWithIdAsPrimaryKey[EmailConfirmEntity, EmailConfirms](TableQuery[EmailConfirms]) {
}