package com.nichoshop.swaggers

import com.nichoshop.models.{Messages, MessageEntity, MessageFolderEntity, MessageFolders}
import org.scalatra.swagger.SwaggerSupport


/**
 * Created by Nursultan on 6/6/2022
 */

trait MessageOperations extends SwaggerSupport {

  def name: String = "Message"

  override protected def applicationDescription: String = "Message"

  val findById = (apiOperation[List[MessageEntity]]("findById")
    summary s"Find a message by id"

    parameters(
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("limit").description("messages per page"))
    )

  val deleteById = (apiOperation[Unit]("deleteById")
    summary s"Delete a message by id"

    parameters(
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("limit").description("messages per page"))
    )
  
  val moveFolder = (apiOperation[Unit]("moveFolder")
    summary s"Move messages to a folder by ids"

    parameters(
      bodyParam[List[Int]].description("Array of marked message ids"),
      queryParam[Int]("folderId").description("folder id")
    )
  )

  val moveTrash = (apiOperation[Unit]("moveTrash")
    summary s"Move messages to a trash by ids"

    parameter  bodyParam[List[Int]].description("Array of marked message ids"),
    
  )
  val getAllCount = (apiOperation[Int]("getAllCount")
    summary s"Get number of all messages"

    )

  val findAll = (apiOperation[List[MessageEntity]]("findAll")
    summary s"Return all user's message"
    
    parameters(
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("limit").description("messages per page"))
    )
  val findInbox = (apiOperation[List[MessageEntity]]("findInbox")
    summary s"Return all user's inbox"
    
    parameters(
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("limit").description("messages per page"))
    )
  
  val findInSent = (apiOperation[List[MessageEntity]]("findInSent")
    summary s"Return all user's sent messages"
    
    parameters(
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("limit").description("messages per page"))
    )
  
  val findInTrash = (apiOperation[List[MessageEntity]]("findInTrash")
    summary s"Return all user's trash messages"
    
    parameters(
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("limit").description("messages per page"))
    )
  val findInFolder = (apiOperation[List[MessageEntity]]("findInFolder")
    summary s"Return all user's certain Folder messages"
    
    parameters(
      queryParam[Int]("folderid").description("folder id"),
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("limit").description("messages per page"))
    )
  val searchByKeyword = (apiOperation[List[MessageEntity]]("searchByKeyword")
    summary s"Search messages by keyword"
    
    parameters(
      queryParam[Int]("keyword").description("keyword for search"),
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("limit").description("messages per page"))
    )
  val searchBySenderName = (apiOperation[List[MessageEntity]]("searchBySenderName")
    summary s"Search messages by sender name"
    
    parameters(
      queryParam[Int]("senderName").description("username of the sender"),
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("limit").description("messages per page"))
    )
  val searchByItemId = (apiOperation[List[MessageEntity]]("searchByItemId")
    summary s"Search messages by sender name"
    
    parameters(
      queryParam[Int]("itemId").description("item id"),
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("limit").description("messages per page"))
    )
  val markAsRead = (apiOperation[Unit]("markAsRead")
    summary s"Mark message as read"
    
    parameters(
      bodyParam[List[Int]].description("Array of marked message ids"),
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("limit").description("messages per page"))
    )

  val markAsUnread = (apiOperation[Unit]("markAsUnread")
    summary s"Mark message as unread"
    
    parameters(
      bodyParam[List[Int]].description("Array of marked message ids"),
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("limit").description("messages per page"))
    )
  val highPriority = (apiOperation[List[MessageEntity]]("highPriority")
    summary s"Returns user's high priority mails"
    
    parameters(
      bodyParam[List[Int]].description("Array of marked message ids"),
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("limit").description("messages per page"))
    )
  val lowPriority = (apiOperation[List[MessageEntity]]("lowPriority")
    summary s"Returns user's low priority mails"
    
    parameters(
      bodyParam[List[Int]].description("Array of marked message ids"),
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("limit").description("messages per page"))
    )

  val createFolder = (apiOperation[Unit]("createFolder")
    summary s"Adds new folder to user's inbox"
    
    parameter queryParam[String]("title").description("Folder name")
    )

  val deleteFolderById = (apiOperation[Unit]("deleteFolderById")
    summary s"Deletes folder by id"
    
    parameter queryParam[Int]("folderId").description("folder id")
    )

  val renameFolder = (apiOperation[Unit]("renameFolder")
    summary s"Rename folder by id"
    
    parameters (
      queryParam[Int]("folderId").description("folder id"),
      queryParam[String]("title").description("title of folder")
    )
  )
  val folderList = (apiOperation[List[MessageFolderEntity]]("folderList")
    summary s"Returns user's mailbox folders"
    
    )
}
