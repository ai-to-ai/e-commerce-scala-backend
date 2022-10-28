package com.nichoshop.legacy.dao.slick

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.legacy.dao.MessageDAO
import com.nichoshop.legacy.models.MessagesRow
import com.nichoshop.legacy.slick.Tables._

class SlickMessageDAO(val db: Database) extends MessageDAO {

  val nichoShop = "NichoShop"

  // folderId=0 is for allMessages folder
  private def messages(userId: Int, folderId: Int = 0) = Messages.filter(m => m.toId === userId && m.folderId === folderId)

  private def mes4space(userId: Int, searchSpace: Int) = searchSpace match {
    case -3 => messages(userId).filterNot(_.fromUserid === "NichoShop")
    case -4 => messages(userId).filter(_.fromUserid === "NichoShop")
    case -5 => messages(userId).filter(_.flag)
    case i => messages(userId, i)
  }

  def searchByKeyword(userId: Int, keyword: String, searchSpace: Int, from: Int, size: Int): List[MessagesRow] =
    db.withSession { implicit session =>
      mes4space(userId, searchSpace).
        filter(m => m.subject.like(s"%$keyword%") || m.itemTitle.like(s"%$keyword%"))
        .sortBy(_.creationTime.desc).drop(from).take(size).list
    }

  def searchBySenderName(userId: Int, senderName: String, searchSpace: Int, from: Int, size: Int): List[MessagesRow] =
    db.withSession { implicit session =>
      mes4space(userId, searchSpace).
        filter(_.fromUserid.like(s"%$senderName%")).sortBy(_.creationTime.desc).drop(from).take(size).list
    }

  def searchByItemId(userId: Int, itemId: Int, searchSpace: Int, from: Int, size: Int): List[MessagesRow] =
    db.withSession { implicit session =>
      mes4space(userId, searchSpace).filter(_.itemId === itemId).sortBy(_.creationTime.desc).drop(from).take(size).list
    }

  def getAllByUserId(userId: Int, from: Int, size: Int): List[MessagesRow] = db.withSession { implicit session =>
    messages(userId).sortBy(_.creationTime.desc).drop(from).take(size).list
  }

  def fromNichoShop(userId: Int, from: Int, size: Int): List[MessagesRow] = db.withSession { implicit session =>
    messages(userId).filter(_.fromUserid === "NichoShop").sortBy(_.creationTime.desc).drop(from).take(size).list
  }

  def fromMembers(userId: Int, from: Int, size: Int): List[MessagesRow] = db.withSession { implicit session =>
    messages(userId).filterNot(_.fromUserid === "NichoShop").sortBy(_.creationTime.desc).drop(from).take(size).list
  }

  def highPriority(userId: Int, from: Int, size: Int): List[MessagesRow] = db.withSession { implicit session =>
    messages(userId).filter(_.flag).sortBy(_.creationTime.desc).drop(from).take(size).list
  }

  def fromFolder(userId: Int, folderId: Int, from: Int, size: Int): List[MessagesRow] = db.withSession { implicit session =>
    messages(userId, folderId).sortBy(_.creationTime.desc).drop(from).take(size).list
  }

  def moveToFolder(userId: Int, ids: List[Int], folderId: Int) = db.withSession { implicit session =>
    messages(userId).filter(_.id inSetBind ids).map(_.folderId).update(folderId)
  }

  def markAsRead(userId: Int, ids: List[Int]) = db.withSession { implicit session =>
    messages(userId).filter(_.id inSetBind ids).map(_.msgRead).update(true)
  }

  def markAsUnread(userId: Int, ids: List[Int]) = db.withSession { implicit session =>
    messages(userId).filter(_.id inSetBind ids).map(_.msgRead).update(false)
  }

  def markAsFlagged(userId: Int, ids: List[Int]) = db.withSession { implicit session =>
    messages(userId).filter(_.id inSetBind ids).map(_.flag).update(true)
  }

  def markAsUnflagged(userId: Int, ids: List[Int]) = db.withSession { implicit session =>
    messages(userId).filter(_.id inSetBind ids).map(_.flag).update(false)
  }

  def create(x: MessagesRow) = db.withSession { implicit session =>
    Messages += x
  }

  def deleteById(userId: Int, mid: Int) = db.withSession { implicit session =>
//    messages(userId).filter(_.id === mid).delete
  }

  def findById(userId: Int, mid: Int): Option[MessagesRow] = db.withSession { implicit session =>
    messages(userId).filter(_.id === mid).take(1).list.headOption
  }
}
