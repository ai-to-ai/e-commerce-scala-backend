package com.nichoshop.controllers

import com.nichoshop.auth.AuthenticationSupport
import com.nichoshop.utils.NotAuthorizedException
import com.nichoshop.services.Services
import org.scalatra.{Forbidden, ScalatraServlet}
import javax.servlet.http.HttpServletRequest



abstract class AuthController extends BaseController with AuthenticationSupport  {

  def ifEnoughRights(block: => Any): Any = {
    if (Services.authService.fromSession(session.getId).exists(_.getId == params("id").toInt)) block
    else Forbidden("Insufficient rights")
  }

  def uid: Int = Services.authService.fromSession(session.getId).map(_.getId).getOrElse(throw new NotAuthorizedException)
  
}
