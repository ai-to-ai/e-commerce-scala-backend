package com.nichoshop.controllers.admin

import com.nichoshop.utils.{UserNotFoundException, SecureUtils, Twilio, Constants, Marshallers}
import com.nichoshop.Environment.duo.{apiHost, clientId, clientSecret, redirectSUCUri}
import com.nichoshop.Environment
import com.nichoshop.models.{DuoConfirmEntity, SessionEntity}
import com.nichoshop.services.Services
import com.nichoshop.controllers.AuthController
import com.duosecurity.Client
import com.duosecurity.model.Token
import javax.servlet.http.HttpSession

class LoginTempController extends AuthController{

  def name = "admin/login-temp"

  val usersService = Services.usersService
  val auth = Services.authService
  val sucService = Services.sucService
  val duoConfirmService  = Services.duoConfirmService
  val duoClient = new Client.Builder(clientId, clientSecret, apiHost, redirectSUCUri).build

  post("/")({
    duoClient.healthCheck
    val userName = Environment.adminId
    val state = duoClient.generateState
    val duoConfirmEntity = new DuoConfirmEntity(id = None, userName = userName, state = state)

    // Save to database                              
    duoConfirmService.create(duoConfirmEntity)

    // Response
    Marshallers.data( duoClient.createAuthUrl(userName, state))
  })

  get("/request")({
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

              // Send SUC
              val usedSUCList = sucService.getSUCsByUserId(user.id.get)

              val suc = SecureUtils.generateSUC(usedSUCList)

              sucService.createSUC(user.id.get, suc, Constants.sucType.adminTemp)

              if(suc.length>0){
                Twilio.sendSMSToNumber(user.phone.getOrElse("19852362031"), suc)
                Marshallers.ok()
              }
              else Marshallers.bad("Unable to create SUC")

            case None => Marshallers.bad("USER NOT EXIST")
          }
        } else Marshallers.bad("2FA FAILED")
      }
      case None => Marshallers.bad("STATE NOT EXIST")
    }
  })

  post("/request/confirm")({

    val temporaryPasswordConfirm = parsedBody.extract[TemporaryPasswordConfirm]

    usersService.findByEmailOrUserid(Environment.adminId) match {
      case Some(user) =>

        sucService.findLastOne(user.id.get, Constants.sucType.adminTemp) match {
          
          case Some(sucData)=> 
            if(sucData.code == temporaryPasswordConfirm.suc){

              val duration = System.currentTimeMillis() - sucData.created
              if( duration <= Constants.sucExpire.adminTemp){

                  // Create a session if no exist.
                  val httpSession: HttpSession = request.getSession(true)
                  val session = SessionEntity(None, Environment.adminId, httpSession.getId, System.currentTimeMillis)
                  auth.saveSession(session)

                   Marshallers.redirect("https://nichoshop.com/admin")

              } else Marshallers.bad("SUC code expired.")
              
            } else Marshallers.bad("SUC code invalid.")

          case None => Marshallers.bad("Unable to find SUC")
        }
      
      case None => Marshallers.bad("User not found")
    }
  })

  private def authWasSuccessful(token: Token): Boolean = {
    token != null && token.getAuth_result != null && "ALLOW".equalsIgnoreCase(token.getAuth_result.getStatus)
  }
}

case class TemporaryPasswordConfirm(suc: String)
