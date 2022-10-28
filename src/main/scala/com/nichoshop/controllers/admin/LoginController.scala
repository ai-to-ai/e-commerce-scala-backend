package com.nichoshop.controllers.admin

import com.nichoshop.services.Services
import com.nichoshop.controllers.AuthController
import com.nichoshop.utils.{CaptchaClient, SecureUtils, Constants,Cookies, Marshallers}
import com.duosecurity.Client
import com.duosecurity.model.Token
import com.nichoshop.Environment.duo.{apiHost, clientId, clientSecret, redirectUri}
import com.nichoshop.models.{DuoConfirmEntity, SessionEntity}
import com.nichoshop.Environment

import javax.servlet.http.HttpSession

class LoginController extends AuthController {

  def name="admin/login"

  val duoConfirmService = Services.duoConfirmService
  val usersService = Services.usersService
  val auth = Services.authService
  val duoClient = new Client.Builder(clientId, clientSecret, apiHost, redirectUri).build

  post("/")( {
    val ip = getIpFromRequest

    val login = params.getOrElse("login","")
    val password = params.getOrElse("password","")

    val captcha = true //!Services.auth.captureRequired(ip) || params.get("grecaptcha").exists(CaptchaClient.checkCaptchaV2)
    if (captcha){
      auth.login(login,password) match {
          case Some(user) =>{
            if (!usersService.hasAdminOrCustomerSupportRole(user)) {
              Marshallers.bad("ALLOW_LOGIN_ONLY_ADMIN_OR_CUSTOMER_SUPPORT")
            }
            /**
             * Duo Client Check.
             */
            duoClient.healthCheck
            val userName = user.getUserid
            val state = duoClient.generateState
            val duoConfirmEntity = new DuoConfirmEntity(id = None, userName = userName, state = state)

            // Save to database                              
            duoConfirmService.create(duoConfirmEntity)

            // Response
             Marshallers.data(duoClient.createAuthUrl(userName, state))
          }
          case None => Marshallers.bad("Auth Failed.")
        }
      }
    else {
      unauthorized("Captcha check failed", capchaRequired = auth.captureRequired(ip))
    }
  })

  get("/confirm")( {
     val state = params.getOrElse("state","")
    val duoCode = params.getOrElse("duo_code", "")

    duoConfirmService.getUserNameByState(state) match {

      case Some(userName) => {

        // Verify DUO 2FA
        val token = duoClient.exchangeAuthorizationCodeFor2FAResult(duoCode, userName) 
        if (authWasSuccessful(token)) {

          // Delete State from Database
          duoConfirmService.getUserByState(state) match {
            case Some(user) =>
                  duoConfirmService.deleteByState(state)

                  // Create a session if no exist.
                  val httpSession: HttpSession = request.getSession(true)
                  val session = SessionEntity(None, Environment.adminId, httpSession.getId, System.currentTimeMillis)
                  auth.saveSession(session)

                  // Save a Login log.
                  

                  val userData = Map(
                                    "id"->user.id,
                                    "username" -> user.userid,
                                    "email"-> user.email)
                  val token = SecureUtils.generateJWTToken(Constants.tokenSecret, Constants.tokenExpire,userData)
                  
                  Marshallers.redirect(s"http://localhost:3000/admin?login=true&data=${token}")

            case None => Marshallers.bad("USER NOT EXIST")
          }
        } else Marshallers.bad("2FA FAILED")
      }
      case None => Marshallers.bad("STATE NOT EXIST")
    }
  })

  get("/status") {
    val ip = getIpFromRequest
    val token = params.getOrElse("data","")

    val claim = SecureUtils.verifyJWTToken(Constants.tokenSecret,token)
    if(claim.contains("id")){
      auth.fromSession(session.getId).
        orElse {
        Cookies.withTokenCookie(auth.getUserByTokenHashSession)
      }.map(Marshallers.toJson).
        getOrElse(Marshallers.unauthorized("Not logged in",Map("capture-required"->auth.captureRequired(ip).toString)))
    }
  }

  post("/logout") {
    scentry.logout()
    Marshallers.ok()
  }

  private def authWasSuccessful(token: Token): Boolean = {
    token != null && token.getAuth_result != null && "ALLOW".equalsIgnoreCase(token.getAuth_result.getStatus)
  }

  private def unauthorized(message: String, capchaRequired: Boolean = true) = {
    Marshallers.unauthorized(message, Map("grecaptcha-required" -> capchaRequired.toString))
  }

  case class LoginBody(login: String, password: String)
}
