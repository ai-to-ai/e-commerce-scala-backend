package com.nichoshop.legacy.dao.slick

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.legacy.dao.MessageFolderDAO
import com.nichoshop.legacy.models.MessageFoldersRow
import com.nichoshop.legacy.slick.Tables._

class SlickMessageFolderDAO(val db: Database) extends MessageFolderDAO {

  def create(x: MessageFoldersRow) = db.withSession { implicit session =>
    MessageFolders += x
  }

  def deleteById(userId: Int, id: Int) = db.withSession { implicit session =>
//    MessageFolders.filter(_.userId === userId).filter(_.id === id).delete
  }

  def getAllByUserId(userId: Int): List[MessageFoldersRow] = db.withSession { implicit session =>
    MessageFolders.filter(_.userId === userId).sortBy(_.name).list
  }
}
