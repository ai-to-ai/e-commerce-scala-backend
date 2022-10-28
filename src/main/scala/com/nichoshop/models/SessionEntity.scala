package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import scala.slick.lifted.Tag

/**
 * Created by Evgeny Zhoga on 24.06.15.
 */
case class SessionEntity (

          override val id:Option[Int] = None,
          userId: String,
          hash: String,
          creationTime: Long  = System.currentTimeMillis()
          
) extends IdAsPrimaryKey

class Sessions(tag:Tag) extends TableWithIdAsPrimaryKey[SessionEntity](tag, "sessions") {
  def userId = column[String]("userid")

  def hash = column[String]("hash")

  def creationTime = column[Long]("creation_time")

  def uniqueHashIndex = index("unique_hash", hash, true)

  def * = (id.?, userId, hash, creationTime) <>(SessionEntity.tupled, SessionEntity.unapply)
}

object Sessions extends QueryForTableWithIdAsPrimaryKey[SessionEntity, Sessions](TableQuery[Sessions]) {
  def findByHash(hash: String): Option[SessionEntity] = DB.read ({ implicit s =>
    query.filter(_.hash === hash).firstOption
  })

  def deleteByHash(hash: String): Int = DB.write ({ implicit s =>
    query.filter(_.hash === hash ).delete
  })

}

