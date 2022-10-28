package com.nichoshop.legacy.dao.slick

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.legacy.dao.SessionDAO
import com.nichoshop.legacy.models.SessionsRow
import com.nichoshop.legacy.slick.Tables._


class SlickSessionDAO(val db: Database) extends SessionDAO {
  def create(x: SessionsRow) = db.withSession { implicit session =>
    Sessions += x
  }

  def updateById(id: Int, x: SessionsRow) = db.withSession { implicit session =>
    Sessions.filter(_.id === id).update(x.copy(id = id))
  }

  def findById(id: Int): Option[SessionsRow] = db.withSession { implicit session =>
    Sessions.filter(_.id === id).list.headOption
  }

  def findAll: List[SessionsRow] = db.withSession { implicit session =>
    Sessions.list
  }

  def deleteById(id: Int) = db.withSession { implicit session =>
//    Sessions.filter(_.id === id).delete
  }

  def deleteByHash(hash: String) = db.withSession { implicit session =>
//    Sessions.filter(_.hash === hash).delete
  }
}
