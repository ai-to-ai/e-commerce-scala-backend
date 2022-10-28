package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 6/6/2022
 */

case class MessageFolderEntity(

            override val id: Option[Int] = None,
            title: String, 
            userId: Int,
            created: Long = System.currentTimeMillis()

) extends IdAsPrimaryKey

class MessageFolders(tag:Tag) extends TableWithIdAsPrimaryKey[MessageFolderEntity](tag, "message_folders") {

  def title = column[String]("title")
  def userId = column[Int]("user_id")
  def created = column[Long]("created")

  def * = (id.?, title, userId, created) <> (MessageFolderEntity.tupled, MessageFolderEntity.unapply)
}

object MessageFolders extends QueryForTableWithIdAsPrimaryKey[MessageFolderEntity, MessageFolders](TableQuery[MessageFolders]) {
}