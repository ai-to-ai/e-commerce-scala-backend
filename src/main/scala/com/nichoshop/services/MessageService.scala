package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers.DB
import com.nichoshop.models.{MessageEntity, Messages}
import com.nichoshop.utils.Pagination

import org.slf4j
import org.slf4j.LoggerFactory

/** 
 *  Created by Nursultan on 6/6/2022
 */

class MessageService {
    protected val log: slf4j.Logger = LoggerFactory.getLogger(getClass)

    val messages = Messages.query

    def findById(id: Int) : Option[MessageEntity] = DB.read{ implicit session =>
        messages.filter(_.id === id).take(1).list.headOption
    }

    def findAllCount(userId: Int) : Int = DB.read{ implicit session =>
        messages.filter(m => m.fromId === userId || m.toId === userId).list.size
    }
    
    def findAll(userId: Int)(implicit paging : (Int, Int)) : List[MessageEntity] = DB.read{ implicit session =>
        val (page, limit) = paging
        messages.filter(m => m.fromId === userId || m.toId === userId)
                .sortBy(_.created.desc)
                .drop((page-1)*limit)
                .take(limit).list
    }
    
    def findInbox(userId: Int)(implicit paging : (Int, Int)): List[MessageEntity] = DB.read{ implicit session =>
       val (page, limit) = paging
        messages.filter(_.toId === userId)
                .sortBy(_.created.desc)
                .drop((page-1)*limit)
                .take(limit).list 
    }
    
    def findInSent(userId: Int)(implicit paging : (Int, Int)): List[MessageEntity] = DB.read{ implicit session =>
        val (page, limit) = paging
        messages.filter(_.fromId === userId)
                .sortBy(_.created.desc)
                .drop((page-1)*limit)
                .take(limit).list 
    }

    def findInTrash(userId: Int)(implicit paging : (Int, Int)): List[MessageEntity] = DB.read{ implicit session =>
        val (page, limit) = paging
        messages.filter(m => m.fromId === userId || m.toId === userId)
                .filter(m => m.flag === false)
                .sortBy(_.created.desc)
                .drop((page-1)*limit)
                .take(limit).list
    }

    def findInFolder(fId: Int)(implicit paging : (Int, Int)) : List[MessageEntity] = DB.read{ implicit session =>
        val (page, limit) = paging
        messages.filter(m => m.folderId === fId)
                .sortBy(_.created.desc)
                .drop((page-1)*limit)
                .take(limit).list
    }

    def searchByKeyword(userId: Int, keyword: String)(implicit paging : (Int, Int)) : List[MessageEntity] = DB.read{ implicit session =>
        val (page, limit) = paging
        messages.filter(m => m.subject.like(s"%$keyword%") || m.itemTitle.like(s"%$keyword%"))
                .sortBy(_.created.desc)
                .drop((page-1)*limit)
                .take(limit).list
    }
    
    def searchBySenderName(userId: Int, senderName: String)(implicit paging : (Int, Int)): List[MessageEntity] = DB.read { implicit session =>
        val (page, limit) = paging
        messages.filter(_.senderName.like(s"%$senderName%"))
                .sortBy(_.created.desc)
                .drop((page-1)*limit)
                .take(limit).list
    }

    def searchByItemId(userId: Int, itemId: Int)(implicit paging : (Int, Int)): List[MessageEntity] = DB.write { implicit session =>
        val (page, limit) = paging
        messages.filter(_.itemId === itemId)
                .sortBy(_.created.desc)
                .drop((page-1)*limit)
                .take(limit).list
    }

    
    def highPriority(ids:List[Int])(implicit paging : (Int, Int)): List[MessageEntity]  = DB.read { implicit session =>
        val (page, limit) = paging

        messages.filter(m => m.id inSetBind ids)
                .filter(m => m.priority === true)
                .sortBy(_.created.desc)
                .drop((page-1)*limit)
                .take(limit).list    
    }

    def lowPriority(ids:List[Int])(implicit paging : (Int, Int)): List[MessageEntity]  = DB.read { implicit session =>
        val (page, limit) = paging
        messages.filter(m => m.id inSetBind ids)
                .filter(m => m.priority === false)
                .sortBy(_.created.desc)
                .drop((page-1)*limit)
                .take(limit).list 
    }
    
    
    def markAsRead(ids: List[Int]) = DB.write { implicit session =>
        messages.filter(m => m.id inSetBind ids)
        .map(_.read)
        .update(true)    
    }
    def markAsUnread(ids: List[Int]) = DB.write { implicit session =>
        messages.filter(m => m.id inSetBind ids)
        .map(_.read)
        .update(false)    
    }
    
    def moveFolder(ids: List[Int], fId: Int) = DB.write{ implicit session =>
        messages.filter(m => m.id inSetBind ids)
                .map(_.folderId)
                .update(fId)
    }
            
    def moveToTrash(ids:List[Int]) = DB.write{ implicit session =>
        messages.filter(m => m.id inSetBind ids)
        .map(_.flag)
        .update(false)
    }

    def create(x: MessageEntity) : Unit = DB.write{ implicit session =>
        messages.insert(x)
    }
}