package com.nichoshop.auth.strategies

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.model.dto.UserDto
import com.nichoshop.models._
import com.nichoshop.models.helpers.DB
import com.nichoshop.services.Services
import com.nichoshop.utils.Cookies
import org.scalatra.auth.ScentryStrategy
import org.scalatra.{CookieOptions, ScalatraBase}
import org.slf4j.LoggerFactory

import java.util.concurrent.TimeUnit
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

class RememberMeStrategy(protected val app: ScalatraBase)
                        (implicit request: HttpServletRequest, response: HttpServletResponse)
  extends ScentryStrategy[UserDto] {

  val logger = LoggerFactory.getLogger(getClass)

  override def name: String = "RememberMe_Nichoshop"

  val COOKIE_KEY = Cookies.REMEMBER_ME
  private val oneWeek = TimeUnit.DAYS.toSeconds(7).toInt
  
  private def tokenVal = app.cookies.get(COOKIE_KEY).filter(_.contains(".")).map(s => {
    logger.info(s" >>>>> get cookie $s")
    val v = s.split('.')
    logger.info(s" >>>>> length: ${v.length}, $v")
    v(0) -> v(1)
  })
  override def isValid(implicit request: HttpServletRequest): Boolean = {
    val t = tokenVal
    logger.info(s"${app.cookies.get(COOKIE_KEY)}")
    logger.info("RememberMeStrategy: determining isValid: " + t.isDefined)
    t.isDefined
    false
  }

  def authenticate()(implicit request: HttpServletRequest, response: HttpServletResponse) = {
    logger.info("RememberMeStrategy: attempting authentication")
    Cookies.withTokenCookie( Services.authService.getUserByTokenHashSession )
  }

  /** *
    * After successfully authenticating with either the RememberMeStrategy, or the UserPasswordStrategy with the
    * "remember me" tickbox checked, we set a rememberMe cookie for later use.
    *
    * NB make sure you set a cookie path, or you risk getting weird problems because you've accidentally set
    * more than 1 cookie.
    */
  override def afterAuthenticate(winningStrategy: String, user: UserDto)(implicit request: HttpServletRequest, response: HttpServletResponse) = {
    logger.info("rememberMe: afterAuth fired")
    logger.info(s">>>> winningStrategy=$winningStrategy, rememberMe: ${app.params.get("rememberMe")}")
    // if (winningStrategy == "RememberMe" ||
    //   checkbox2boolean(app.params.getOrElse("rememberMe", ""))) {
    //   logger.info("rememberMe: we are going to set rememberMe token")
    //   Cookies.newTokenCookie(user.getUserid)

    // }
  }
  private def setCookie(token: TokenEntity)(implicit request: HttpServletRequest, response: HttpServletResponse): Unit = {
    app.cookies.set(COOKIE_KEY, s"${token.hashSession}.${token.hash}")(CookieOptions(maxAge = oneWeek, path = "/"))
  }
  private def deleteCookie(implicit request: HttpServletRequest, response: HttpServletResponse): Unit = {
    app.cookies.delete(COOKIE_KEY)(CookieOptions(path = "/"))
  }

  /**
   * Run this code before logout, to clean up any leftover database state and delete the rememberMe token cookie.
   */
  override def beforeLogout(user: UserDto)(implicit request: HttpServletRequest, response: HttpServletResponse) = {

    logger.info("rememberMe: beforeLogout")
    tokenVal.foreach {case (hashSession, hash) =>
        DB.write {implicit s => Tokens.query.filter(_.hashSession === hashSession).delete}
    }
    deleteCookie
  }


  /**
   * Used to easily match a checkbox value
   */
  private def checkbox2boolean(s: String): Boolean = {
    s match {
      case "yes" => true
      case "y" => true
      case "1" => true
      case "true" => true
      case _ => false
    }
  }
}
