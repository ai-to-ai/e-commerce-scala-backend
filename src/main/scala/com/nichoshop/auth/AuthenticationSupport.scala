package com.nichoshop.auth

import com.nichoshop.auth.strategies._
import com.nichoshop.model.dto.UserDto
import com.nichoshop.services.Services
import org.scalatra.auth.{ScentryConfig, ScentrySupport}
import org.scalatra.{ScalatraBase, Unauthorized}
import org.slf4j.LoggerFactory


trait AuthenticationSupport extends ScalatraBase with ScentrySupport[UserDto] {

  protected def fromSession = {
    case id: String => Services.authService.fromSession(id).orNull
  }

  protected def toSession = {
    case usr: UserDto => Services.authService.toSession(usr).map(_.getId).orNull
  }

  protected val scentryConfig = new ScentryConfig {}.asInstanceOf[ScentryConfiguration] //todo remove overriding

  val logger = LoggerFactory.getLogger(getClass)

  protected def requireLogin() = {
    if (!isAuthenticated) {
      halt(Unauthorized(body = "-1"))
    }
  }

  override protected def configureScentry() = {
    scentry.unauthenticated {
      requireLogin()
    }
  }

  /**
   * Register auth strategies with Scentry. Any controller with this trait mixed in will attempt to
   * progressively use all registered strategies to log the user in, falling back if necessary.
   */
  override protected def registerAuthStrategies(): Unit = {
    scentry.register("RememberMe", app => new RememberMeStrategy(app))
    scentry.register("UsernamePassword", app => new UserPasswordStrategy(app))
  }

}