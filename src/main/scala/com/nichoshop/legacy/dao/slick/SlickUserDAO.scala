package com.nichoshop.legacy.dao.slick

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.legacy.dao.UserDAO
import com.nichoshop.legacy.models.{EmailConfirmationRow, SessionsRow, TokensRow, UsersRow}
import com.nichoshop.legacy.slick.Tables._

import scala.slick.jdbc.StaticQuery.interpolation

class SlickUserDAO(val db: Database) extends UserDAO {

  def findById(id: Int): Option[UsersRow] = db.withSession { implicit session =>
    Users.filter(_.id === id).take(1).list.headOption
  }

  def findByUserId(userid: String): Option[UsersRow] = db.withSession { implicit session =>
    Users.filter(_.userid === userid).take(1).list.headOption
  }

  def updateById(id: Int, x: UsersRow) = db.withSession { implicit session =>
    Users.filter(_.id === id).update(x.copy(id = id))
  }

  def findAll: List[UsersRow] = db.withSession { implicit session =>
    Users.list
  }

  def create(x: UsersRow) = db.withSession { implicit session =>
    Users += x
  }

  def deleteById(id: Int) = db.withSession { implicit session =>
//    Users.filter(_.id === id).delete
  }

  def deleteByUserId(userid: String) = db.withSession { implicit session =>
//    Users.filter(_.userid === userid).delete
  }

  def updateByUserId(user: UsersRow) = db.withSession { implicit session =>
    Users.filter(_.userid === user.userid).map(_.id).run.headOption.map(i => //TODO rewrite to long version but in 1 query
      Users.filter(_.userid === user.userid).update(user.copy(id = i))
    )
  }

  def findByEmail(email: String): Option[UsersRow] = db.withSession { implicit session =>
    Users.filter(_.email === email).list.headOption
  }

  def findByPhone(phone: String): Option[UsersRow] = db.withSession { implicit session =>
    Users.filter(_.phone === phone).list.headOption  
  }

  def fromSession(s: String): Option[UsersRow] = db.withSession { implicit session =>
    (Sessions innerJoin Users on (_.userid === _.userid) filter (_._1.hash === s) map (_._2)).list.headOption
  }

  def toSession(userid: String): Option[SessionsRow] = db.withSession { implicit session =>
    (Sessions innerJoin Users on (_.userid === _.userid) filter (_._2.userid === userid) map (_._1)).list.headOption
  }

  def fromToken(hash: String): Option[UsersRow] = db.withSession { implicit session =>
    (Tokens innerJoin Users on (_.userid === _.userid) filter (_._1.hash === hash) map (_._2)).list.headOption
  }

  def toToken(user: UsersRow): Option[TokensRow] = db.withSession { implicit session =>
    (Tokens innerJoin Users on (_.userid === _.userid) filter (_._2.userid === user.userid) map (_._1)).list.headOption
  }

  def saveEmailConfirmationCode(emailConfirmation: EmailConfirmationRow) = db.withSession { implicit session =>
    EmailConfirmation += emailConfirmation
  }

  def confirmEmail(code: String): Boolean = db.withSession { implicit session =>
    session.withTransaction {
      val row = sqlu"""UPDATE users u JOIN email_confirmation e ON u.id = e.user_id
    SET u.email_confirmed = TRUE
    WHERE e.code = $code AND u.email_confirmed = FALSE""".first

     if (row != 0) sqlu"DELETE FROM email_confirmation WHERE code=$code"
    //  if (row != 0) EmailConfirmation.filter(_.code === code).delete

      row > 0
    }
  }
}
