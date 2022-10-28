package com.nichoshop.swaggers

import com.nichoshop.legacy.models.UsersRow
import org.scalatra.swagger.SwaggerSupport

trait SessionOperations extends SwaggerSupport {

  def name: String = "Login"

  override protected def applicationDescription: String = "Login"

  val loginStatus = (apiOperation[UsersRow]("status")
    summary s"Returns current user in case if user logged in"
    )

  val login = (apiOperation[UsersRow]("login")

    parameters(formParam[String]("login").description("userid or email"),
    formParam[String]("password").description("password"),
    formParam[Boolean]("rememberMe").description("whether to remember user after session delete"),
    formParam[String]("grecaptcha").description("recapcha v2 value"))
    )

  val loginWithUserNameAndPassword = (
    apiOperation[UsersRow]("loginWithUserNameAndPassword")
      summary "Login to site with username and password"
      tags "Session"

      parameters(
      queryParam[String]("login").description("userId or email"),
      queryParam[String]("password").description("password"),
      queryParam[Boolean]("rememberMe").description("Remember Me").optional,
      queryParam[String]("grecaptcha").description("grecaptcha").optional)
    )

  val logout = (apiOperation[UsersRow]("logout")
    summary "Logout from site"
    tags "Session"
    )

}
