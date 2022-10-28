package com.nichoshop.auth.strategies

import com.nichoshop.model.dto.UserDto
import com.nichoshop.models.SessionEntity
import com.nichoshop.services.Services
import org.scalatra.ScalatraBase
import org.scalatra.auth.ScentryStrategy
import org.slf4j.LoggerFactory
import org.scalatra._
import org.json4s._
import org.scalatra.{CookieOptions, ScalatraBase}
import java.util.concurrent.TimeUnit

import javax.servlet.http.{HttpServletRequest, HttpServletResponse, HttpSession}

class UserPasswordStrategy(protected val app: ScalatraBase)(implicit request: HttpServletRequest, response: HttpServletResponse)
  extends ScentryStrategy[UserDto] {

  val logger = LoggerFactory.getLogger(getClass)

  private val oneWeek = TimeUnit.DAYS.toSeconds(7).toInt

  override def name: String = "UserPassword"

  private def login = app.params.getOrElse("login", "")

  private def password = app.params.getOrElse("password", "")


  /** *
   * Determine whether the strategy should be run for the current request.
   */
  override def isValid(implicit request: HttpServletRequest): Boolean = {
    logger.info("UserPasswordStrategy: determining isValid: " + (login != "" && password != "").toString)
    login != "" && password != ""
  }

  /**
   * In real life, this is where we'd consult our data store, asking it whether the user credentials matched
   * any existing user. Here, we'll just check for a known login/password combination and return a user if
   * it's found.
   */
  def authenticate()(implicit request: HttpServletRequest, response: HttpServletResponse): Option[UserDto] = {
    logger.info("UserPasswordStrategy: attempting authentication")

    Services.authService.login(login, password) match {
      case user@Some(usr) =>
        logger.info(s"UserPasswordStrategy: login succeeded for $login")
        user
      case None =>
        logger.info(s"UserPasswordStrategy: login failed for $login")
        None
    }
  }

  override def afterAuthenticate(winningStrategy: String, user: UserDto)
                                (implicit request: HttpServletRequest, response: HttpServletResponse): Unit = {

    // Create a session if no exist.

    // app.cookies.set("COOKIE_KEY", "12312321321")(CookieOptions(maxAge = oneWeek, path = "/"))                              
    val httpSession: HttpSession = request.getSession(true)
    val session = SessionEntity(None, user.getUserid, httpSession.getId, System.currentTimeMillis)
    Services.authService.saveSession(session)
  }


  override def beforeLogout(user: UserDto)(implicit request: HttpServletRequest, response: HttpServletResponse): Unit = {
    Services.authService.dropSession(request.getSession(false).getId)
  }

  case class LoginBody(login: String, password: String)
}

