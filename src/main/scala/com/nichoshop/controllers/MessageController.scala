package com.nichoshop.controllers

import com.nichoshop.swaggers.MessageOperations
import org.scalatra.swagger.Swagger
import com.nichoshop.services.Services
import com.nichoshop.utils
import com.nichoshop.models.MessageFolderEntity

/**
 * Created by Nursultan 6/6/2022
 */

class MessageController(implicit val swagger: Swagger) extends AuthController with MessageOperations with utils.Pagination {

    val messageService = Services.messageService
    val mFolderService = Services.mFolderService
   
    get("/", operation(findById)) {
        val id = params.getOrElse("id","0").toInt
        messageService.findById(id)
    }
    
    put("/move-folder", operation(moveFolder)) {
        val ids = parsedBody.extract[List[Int]]
        val fId = params.getOrElse("folderId","").toInt
        messageService.moveFolder(ids, fId)
    }

    put("/move-trash", operation(moveTrash)) {
        val ids = parsedBody.extract[List[Int]]
        messageService.moveToTrash(ids)
    }

    get("/all-count", operation(getAllCount)) {
        messageService.findAllCount(uid)
    }
    get("/all", operation(findAll)){
        messageService.findAll(uid)
    }

    get("/inbox", operation(findInbox)){
        messageService.findInbox(uid)
    }
    
    get("/sent", operation(findInSent)) {
        messageService.findInSent(uid)
    }

    get("/trash", operation(findInTrash)) {
        messageService.findInTrash(uid)
    }

    get("/folder", operation(findInFolder)) {
        val fId = params.getOrElse("folderId","0").toInt
        messageService.findInFolder(fId)
    }

    get("/search", operation(searchByKeyword)) {
        val keyword = params.getOrElse("keyword","")
        messageService.searchByKeyword(uid, keyword)
    }

    get("/search/sender-name", operation(searchBySenderName)) {
        val senderName = params.getOrElse("senderName","")
        messageService.searchBySenderName(uid, senderName)
    }

    get("/search/item", operation(searchByItemId)) {
        val itemId = params.getOrElse("itmeId","").toInt
        messageService.searchByItemId(uid,itemId)
    }
    
    put("/read", operation(markAsRead)) {
      messageService.markAsRead(parsedBody.extract[List[Int]])
    }

    put("/unread", operation(markAsUnread)) {
        messageService.markAsUnread(parsedBody.extract[List[Int]])
    }

    get("/high-priority", operation(highPriority)) {
      messageService.highPriority(parsedBody.extract[List[Int]])
    }

    get("/low-priority", operation(lowPriority)) {
        messageService.lowPriority(parsedBody.extract[List[Int]])
    }

    post("/folder", operation(createFolder)) {
        val title = params.getOrElse("title","")
        mFolderService.createFolder(MessageFolderEntity(None, title, uid))
    }

    delete("/folder", operation(deleteFolderById)) {
        val fId = params.getOrElse("folderId","0").toInt
        mFolderService.deleteFolderById(fId)
    }

    put("/folder", operation(renameFolder)) {
        val fId = params.getOrElse("folderId","0").toInt
        val title = params.getOrElse("title","")
        mFolderService.renameFolder(fId, title)
    }

    get("/folder/list",operation(folderList)) {
        mFolderService.folderList(uid)
    }


}