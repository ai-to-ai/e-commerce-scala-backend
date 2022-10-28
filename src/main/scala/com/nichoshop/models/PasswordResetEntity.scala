package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import java.sql.Timestamp
import scala.slick.lifted.Tag

/**
 * Created by Evgeny Zhoga on 13.06.15.
 */
case class PasswordResetEntity (

            override val id:Option[Int] = None,
            userId: Int,
            `type`: String,
            created: Timestamp = new Timestamp(System.currentTimeMillis()),
            hash: Option[String] = None,
            isActive: Boolean = true
            
) extends IdAsPrimaryKey

class PasswordResets(tag:Tag) extends TableWithIdAsPrimaryKey[PasswordResetEntity](tag, "password_reset") {
  def userId = column[Int]("user_id")
  def `type` = column[String]("type")
  def created = column[Timestamp]("created")
  def hash = column[Option[String]]("hash")
  def isActive = column[Boolean]("is_active")

  def * = (id.?, userId, `type`, created, hash, isActive) <> (PasswordResetEntity.tupled, PasswordResetEntity.unapply)
}

object PasswordResets extends QueryForTableWithIdAsPrimaryKey[PasswordResetEntity, PasswordResets](TableQuery[PasswordResets]) {
  object Type {
    val email:String = "email"
    val phoneSms:String = "phone_sms"
    val phoneCall:String = "phone_call"
  }
  def findByHash(hash: String): Option[PasswordResetEntity] = DB.read ({ implicit s =>
    query.filter(_.hash === hash).firstOption
  })
}
