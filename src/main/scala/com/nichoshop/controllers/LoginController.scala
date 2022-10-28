package com.nichoshop.controllers

import com.nichoshop.Environment
import com.nichoshop.services.Services
import com.nichoshop.utils.{SecureUtils, Emailer, Twilio, Constants, Marshallers, CaptchaClient, Cookies}
import com.nichoshop.models.{UserEntity, SessionEntity}
import javax.servlet.http.HttpSession

class LoginController extends AuthController {

  def name = "login"

  val usersService = Services.usersService
  val sucService = Services.sucService
  val auth = Services.authService

  
  post("/") {
    val ip = getIpFromRequest

    // val loginBody = parsedBody.extract[LoginBody]

    val captcha = true //Environment.skipCaptcha || CaptchaClient.checkCaptchaV2("loginBody.grecaptcha")

    if (captcha) {
      scentry.authenticate("UsernamePassword") match {
        case Some(user) => Marshallers.toJson(user)
        case None => Marshallers.bad("Cannnot find user.")
      }
    } else Marshallers.forbidden("Capcha check failed")
  }

  get("/check-user") {
      val user = params.getOrElse("user","")
      val grecaptcha = params.getOrElse("grecaptcha","")

      val captcha = Environment.skipCaptcha || CaptchaClient.checkCaptchaV2(grecaptcha)

      if(captcha){
          usersService.findByEmailOrUserid(user) match {
            case Some(u) => if(u.phoneConfirmed) Marshallers.ok(Map("user"-> user, "email"->u.email, "phone" -> u.phone.get))
                            else Marshallers.ok(Map("user"-> user, "email"->u.email))
            case None => Marshallers.bad("Email or username does not exist.")
          }
      } else  Marshallers.bad("Captcha Failed.")
  }

  post("/get-suc-email"){
    val sucType = params.getOrElse("suc_type","0").toInt

    auth.withUser{user =>() =>{

        val usedSUCList = sucService.getSUCsByUserId(user.getId)
        val suc = SecureUtils.generateSUC(usedSUCList)

        sucService.createSUC(user.getId, suc, sucType)

        if(suc.length>0){
          usersService.findByEmailOrUserid(user.getUserid) match {
            case Some(usr) => Emailer.sendSUCConfirmation(usr, suc)
            case None => Marshallers.bad("Cannot send SUC.")
          }
          
          Marshallers.ok()
        }
        else Marshallers.bad("Unable to create SUC")
      }
    }
  }

  post("/get-suc-text") {
    val sucType = params.getOrElse("suc_type","0").toInt
       auth.withUser{user =>() =>{
          val usedSUCList = sucService.getSUCsByUserId(user.getId)

          val suc = SecureUtils.generateSUC(usedSUCList)

          sucService.createSUC(user.getId, suc, sucType)

          if(suc.length>0){
            Twilio.sendSMSToNumber(user.getPhone, suc)
            Marshallers.ok()
          }
          else Marshallers.bad("Unable to create SUC")
      }
    }

  }

  post("/confirm-suc") {
    val suc = params.getOrElse("suc","")
    val sucType = params.getOrElse("suc_type","0").toInt

    auth.withUser{ user=>() =>
  
        sucService.findLastOne(user.getId, sucType) match {
          
          case Some(sucData)=>   
            if(sucData.code == suc){
              val duration = System.currentTimeMillis() - sucData.created

              if( sucType == Constants.sucType.forgotEmail || 
                  sucType == Constants.sucType.forgotText ) {

                        if(duration <= Constants.sucExpire.forgotPass){
                            
                          // create a new password.
                          // Create JWT
                          val userData = Map(
                                        "id"->user.getId,
                                        "username" -> user.getUserid,
                                        "email"-> user.getEmail)
                          val jwt = SecureUtils.generateJWTToken(Constants.tokenSecret, Constants.tokenExpire,userData)
                          Map("token"->jwt)

                        } else Marshallers.bad("SUC expired.")

              } else if( sucType == Constants.sucType.tempText ||
                          sucType == Constants.sucType.tempEmail ){
                            if(duration <= Constants.sucExpire.tempPass){

                              // Create a session if no exist.
                              val httpSession: HttpSession = request.getSession(true)
                              val session = SessionEntity(None, Environment.adminId, httpSession.getId, System.currentTimeMillis)
                              auth.saveSession(session)

                              Marshallers.toJson(user)
                            } else Marshallers.bad("SUC expired.")          
              } else if(sucType == Constants.sucType.addPhone){
                        if(duration <= Constants.sucExpire.addPhone){

                            // Confirm passsword
                            usersService.confirmPhoneById(Some(user.getId))
                            Marshallers.ok()
                        } else Marshallers.bad("SUC expired.") 
              } 
            } else Marshallers.bad("SUC code invalid.")

          case None => Marshallers.bad("Unable to find SUC")
        }
  
    }
  }

  post("/send-suc") {
    val sucType = params.getOrElse("suc_type","0").toInt

     auth.withUser{user =>() =>{

        val usedSUCList = sucService.getSUCsByUserId(user.getId)
        val suc = SecureUtils.generateSUC(usedSUCList)

        sucService.createSUC(user.getId, suc, sucType)

        if(suc.length>0){
          usersService.findByEmailOrUserid(user.getUserid) match {
            case Some(usr) =>{
              if( sucType == Constants.sucType.forgotEmail || 
                  sucType == Constants.sucType.tempEmail || 
                  sucType == Constants.sucType.adminTemp ) {

                  Emailer.sendSUCConfirmation(usr, suc)
                  
              } else Twilio.sendSMSToNumber(user.getPhone, suc)
              } 
            case None => Marshallers.bad("Cannot send SUC.")
          }
          
          Marshallers.ok()
        }
        else Marshallers.bad("Unable to create SUC")
      }
    }
  }

  post("/add-phone") {

    val phone = params.getOrElse("phone","")

    if(SecureUtils.isValidPhoneNumber(phone)){
      auth.withUser{ user=>() => {
  
          log.info(s"$user")
          val usedSUCList = sucService.getSUCsByUserId(user.getId)
  
          val suc = SecureUtils.generateSUC(usedSUCList)
  
          sucService.createSUC(user.getId, suc, Constants.sucType.addPhone)
  
          if(suc.length>0){
  
              usersService.addPhone(user.getUserid, phone)
  
              Twilio.sendSMSToNumber(phone, suc)
              
              Marshallers.ok()
          } else Marshallers.bad("Unable to create SUC")
        }
      }
    } else Marshallers.bad("Invalid phone number")

  }

  post("/resend-suc") {
    val sucType = params.getOrElse("suc_type","0").toInt
    auth.withUser{ user=>() => {

        val usedSUCList = sucService.getSUCsByUserId(user.getId)

        val suc = SecureUtils.generateSUC(usedSUCList)

        sucService.createSUC(user.getId, suc, sucType)

        if(suc.length>0){

          usersService.findByEmailOrUserid(user.getUserid) match {
            case Some(usr) => {
              sucType match {
                case 0 => Emailer.sendSUCConfirmation(usr, suc)
                case _ => Twilio.sendSMSToNumber(usr.phone.get, suc)
              }
              Marshallers.ok()
            }
            case None => Marshallers.bad("Cannot resend SUC.")
          }
          Marshallers.ok()
        } else Marshallers.bad("Unable to create SUC")
      }
    }
  }

  post("/change-password") {
    val passwordBody = parsedBody.extract[PasswordBody]

    val claim = SecureUtils.verifyJWTToken(Constants.tokenSecret,passwordBody.token) 
    if(claim.getOrElse("username","") == passwordBody.username) {
      
        if(passwordBody.password !="" && passwordBody.confirm !=""){
          if(passwordBody.password == passwordBody.confirm){
    
            usersService.changePassword(passwordBody.username, passwordBody.password)
            Marshallers.ok()
          }  else Marshallers.bad("Confirmation doesn't match.")
        } else Marshallers.bad("Password must have at least one letter.")
    } else Marshallers.bad("Cannot find user info.")
  
  
  }

  get("/status") {
    val ip = getIpFromRequest

    auth.fromSession(session.getId).
      orElse {
      Cookies.withTokenCookie(auth.getUserByTokenHashSession)
    }.map(Marshallers.toJson).
      getOrElse(Marshallers.unauthorized("Not logged in",Map("capture-required"->auth.captureRequired(ip).toString)))
    }

  post("/logout") {
    scentry.logout()
    Marshallers.ok()
  }
}

case class PasswordBody(password: String, confirm: String, username: String, token: String )
case class LoginBody(login: String, password: String, grecaptcha: String)



