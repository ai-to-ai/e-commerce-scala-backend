package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 6/6/2022
 */

case class MessageEntity(

            override val id: Option[Int] = None,
            senderName: String = "",
            fromId: Int, 
            toId: Int, 
            subject: String = "", 
            message: String = "", 
            itemId: Option[Int] = None, 
            itemTitle: Option[String] = None, 
            attached: List[String] = List(),
            replyMsgId: Option[Int] = None,
            orderId: Option[Int] = None,
            read: Boolean = false,
            folderId: Int = 0,
            priority: Boolean = false,
            flag: Boolean = false,
            reason: Option[String] = None,
            messageType: Int = 0,
            created: Long = System.currentTimeMillis()

) extends IdAsPrimaryKey

class Messages(tag:Tag) extends TableWithIdAsPrimaryKey[MessageEntity](tag, "messages") {
  implicit val commaSeparatedString = separatedString()

  def senderName = column[String]("sender_name")
  def fromId = column[Int]("from_id")
  def toId = column[Int]("to_id")
  def subject = column[String]("subject")
  def message = column[String]("message")
  def itemId = column[Option[Int]]("item_id")
  def itemTitle = column[Option[String]]("item_title")
  def attached = column[List[String]]("attached")
  def replyMsgId = column[Option[Int]]("reply_msg_id")
  def orderId = column[Option[Int]]("order_id")
  def read = column[Boolean]("read")
  def folderId = column[Int]("folder_id")
  def priority = column[Boolean]("priority")
  def flag = column[Boolean]("flag")
  def reason = column[Option[String]]("reason")
  def messageType = column[Int]("message_type")
  def created = column[Long]("created")


  def * = (id.?, senderName, fromId, toId, subject, message, itemId, itemTitle, attached, replyMsgId, orderId, read, folderId, priority, flag, reason, messageType, created) <> (MessageEntity.tupled, MessageEntity.unapply)
}

object Messages extends QueryForTableWithIdAsPrimaryKey[MessageEntity, Messages](TableQuery[Messages]) {
}