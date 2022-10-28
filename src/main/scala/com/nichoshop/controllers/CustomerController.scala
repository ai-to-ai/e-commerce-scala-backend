package com.nichoshop.controllers

import com.nichoshop.auth.ControllerAccess
import com.nichoshop.utils.Marshallers
import com.nichoshop.services.Services
import org.scalatra.ScalatraServlet

import javax.servlet.http.HttpServletRequest

/**
 * Created by Evgeny Zhoga on 31.08.15.
 */
abstract class CustomerController extends BaseController {
  implicit val accessType = ControllerAccess.CUSTOMER

  implicit def notAuthorized(implicit request: HttpServletRequest): () => Nothing =
    () => halt(unauthorized("Not logged in",capchaRequired = Services.authService.captureRequired(getIpFromRequest)))

  private def unauthorized(message: String, capchaRequired: Boolean = true) = {
    Marshallers.unauthorized(message, Map("grecaptcha-required" -> capchaRequired.toString))
  }

  protected def mark(name: String, action: CustomerController.Action): Unit = {

  }
  protected def markEnter(name: String) = mark(name, CustomerController.Enter)
  protected def markExit(name: String) = mark(name, CustomerController.Exit)
}

object CustomerController {
  sealed trait Action {
    def name: String
  }
  case object Enter extends Action {
    val name = "ented"
  }
  case object Exit extends Action {
    val name = "exit"
  }

}
