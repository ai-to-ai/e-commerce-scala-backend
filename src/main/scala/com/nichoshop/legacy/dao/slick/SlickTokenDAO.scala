package com.nichoshop.legacy.dao.slick

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.legacy.dao.TokenDAO
import com.nichoshop.legacy.models.TokensRow
import com.nichoshop.legacy.slick.Tables._

class SlickTokenDAO(val db: Database) extends TokenDAO {

  def findById(id: Int): Option[TokensRow] = db.withSession { implicit session =>
    Tokens.filter(_.id === id).list.headOption
  }

  def findByUserId(userid: String): Option[TokensRow] = db.withSession { implicit session =>
    Tokens.filter(_.userid === userid).list.headOption
  }

  def findByHash(hash: String): Option[TokensRow] = db.withSession { implicit session =>
    Tokens.filter(_.hash === hash).list.headOption
  }

  def findAll: List[TokensRow] = db.withSession { implicit session =>
    Tokens.list
  }

  def create(x: TokensRow) = db.withSession { implicit session =>
    Tokens += x
  }

  def deleteById(id: Int) = db.withSession { implicit session =>
//    Tokens.filter(_.id === id).delete
  }

  def deleteByUserId(userid: String) = db.withSession { implicit session =>
//    Tokens.filter(_.userid === userid).delete
  }

  def updateById(id: Int, x: TokensRow) = db.withSession { implicit session =>
    Tokens.filter(_.id === id).update(x.copy(id = id))
  }

  def deleteByHash(hash: String) = db.withSession { implicit session =>
//    Tokens.filter(_.hash === hash).delete
  }
}
