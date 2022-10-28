package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.model.dto.{SessionDto => PublicSession, TokenDto => PublicToken, UserDto => PublicUser}
import com.nichoshop.models
import com.nichoshop.models.helpers.DB
import com.nichoshop.services.memcached.BaseService
import com.nichoshop.Environment
import com.nichoshop.utils.{Conversions, Cookies, NotAuthorizedException}

import org.mindrot.jbcrypt.BCrypt

import javax.servlet.http.{HttpServletRequest, HttpServletResponse, HttpSession}

class AuthService extends BaseService {

  private def getRoles(user: models.UserEntity):List[models.RoleEntity] = {
    DB.read {implicit session => models.Roles.query.filter(_.userId === user.id).list}
  }
  def login(login: String, password: String): Option[PublicUser] = {
    (models.Users.findByEmailOrUserid(login) flatMap { user =>
      if (BCrypt.checkpw(password, user.password)) Some(user)
      else None
    }).map {user =>
      Conversions.toUser(user, getRoles(user))
    }
  }

  def saveSession(session: models.SessionEntity): Unit = DB.write { implicit s =>
    models.Sessions.query.filter(_.userId === session.userId).run.headOption match {
        case Some(ss) => models.Sessions.query.filter(_.userId === session.userId).map(_.hash).update(session.hash)
        case None =>  models.Sessions.insert(session)
    }
  }

  def saveSession(userId: String, sessionId: String): Unit = {
    DB.write { implicit s =>
      models.Sessions.query.filter(_.userId === userId).run.headOption match {
        case Some(ss) => models.Sessions.query.filter(_.userId === userId).map(_.hash).update(sessionId)
        case None =>  models.Sessions.insert(
                          models.SessionEntity(None, userId, sessionId, System.currentTimeMillis)
                      )
      }
    }
  }

  def dropSession(hash: String): Unit = {
    models.Sessions.deleteByHash(hash)
  }

  def fromSession(s: String): Option[PublicUser] = DB.read {implicit session =>
    (for {
      user <- models.Users.query
      s1 <- models.Sessions.query
      if ( user.userid === s1.userId ) && (s1.hash === s)
    } yield user).firstOption.map(u => Conversions.toUser(u, getRoles(u)))
  }

  def getDefaultAdmin(): Option[PublicUser] = DB.read {implicit session =>
    models.Users.query.filter(_.userid === Environment.adminId).firstOption.map(u => Conversions.toUser(u, getRoles(u)))
  }
  def toSession(u: PublicUser): Option[PublicSession] = DB.read { implicit session =>
    (for {
      user <- models.Users.query
      s <- models.Sessions.query
      if ( user.userid === s.userId ) && (user.userid === u.getUserid)
    } yield s).firstOption.map(Conversions.toSession)

  }

  def getUserByTokenHash(hash: String): Option[PublicUser] = DB.read { implicit  session =>
    (for {
      token <- models.Tokens.query
      user <- models.Users.query
      if (token.userid === user.userid) && (token.hash === hash)
    } yield user).firstOption.map(u => Conversions.toUser(u, getRoles(u)))
  }

  def getUserByTokenHashSession(hashSession: String): Option[PublicUser] = DB.read { implicit  session =>
    (for {
      token <- models.Tokens.query
      user <- models.Users.query
      if (token.userid === user.userid) && (token.hashSession === hashSession)
    } yield user).firstOption.map( u => Conversions.toUser(u, getRoles(u)))
  }

  def getTokenByHashSession(hashSession: String): Option[PublicToken] =
    models.Tokens.findByHashSession(hashSession).map(Conversions.toToken)

  //  }models.Tokens.fromToken(hash).map(Conversions.toUser)

//  def toToken(user: MUser): Option[Token] = userDAO.toToken(user)

  def captureRequired(key: String) = true

  def failedLoginCounterInc(key: String): Unit = {
    /*
    models.FailedIpCnts.query.filter(_.ip === String).map(cnt).update(cnt+1)
    */
  }

  def withUser(handler: PublicUser => ( () => Any ))(implicit session: HttpSession, request: HttpServletRequest, response: HttpServletResponse) = {
    log.info(s"${request.getSession(false).getId}")
    Services.authService.fromSession(request.getSession(false).getId).
      orElse {
      Cookies.withTokenCookie(Services.authService.getUserByTokenHashSession)
    }.orElse {
      if(Environment.skipAuth) getDefaultAdmin()
      else throw new NotAuthorizedException()
    }.map(handler(_)()).getOrElse {
      throw new NotAuthorizedException()
    }
  }
}
