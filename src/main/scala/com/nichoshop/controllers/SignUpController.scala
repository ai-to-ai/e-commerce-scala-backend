package com.nichoshop.controllers

import com.nichoshop.Environment
import com.nichoshop.legacy.models.{EmailConfirmationRow, UsersRow}

import com.nichoshop.utils.{Marshallers,SecureUtils, CaptchaClient, Emailer, Constants}
import com.nichoshop.model.dto.AccountType
import com.nichoshop.models.{UserEntity, EmailConfirmEntity}
import com.nichoshop.services.Services
import org.json4s.JsonAST.{JBool, JObject, JString}
import org.scalatra.ScalatraServlet
import java.util.Locale

class SignUpController extends BaseController {

  def name ="signup"

  val usersService = Services.usersService


  post("/") {

    val ip = getIpFromRequest
    val countryCode = "US"
    val signUpUser = parsedBody.extract[SignUpUser]
    val captcha = Environment.skipCaptcha || CaptchaClient.checkCaptchaV2(signUpUser.grecaptcha)

    if (captcha) {
      if(usersService.checkEmailAlreadyExists(signUpUser.email)) Marshallers.bad("Email is already exist.")
      
      if(signUpUser.name.length>0 && signUpUser.password.length >0 && signUpUser.email.length>0){

        // generate username
        var username: String = SecureUtils.generateUsername(signUpUser.name, countryCode)
        var i: Int = 0

        while(usersService.checkUseridAlreadyExists(username) && i < Constants.randomNameCheckIteration){
          username = SecureUtils.generateUsername(signUpUser.name, countryCode)
          i += 1
        }

        // check and create
        if(!usersService.checkUseridAlreadyExists(username)){
          val names = signUpUser.name.split(" ")

          val fname = names(0)
          val lname = names(1)
          if(fname == null || lname == null) Marshallers.bad("Invalid user information.")
          
          val user = new UserEntity(None,username, signUpUser.password, signUpUser.email, fname, lname, System.currentTimeMillis(), accountType = signUpUser.accountType)
    
          usersService.create(user)
          
          // save email confirmation code
          val userId = usersService.findByEmailOrUserid(user.userid).get.id //todo: check bad case when user hadn't been saved
          val code = SecureUtils.generateMD5Token(user.userid)
          usersService.saveEmailConfirm(EmailConfirmEntity(None,userId.get, code))

          // send email
          log.info("sending start.")
          Emailer.sendEmailConfirmation(user, code)
    
          // response
          Marshallers.ok(
            Map("email" -> user.email, "username"->user.userid)
          )
        }
      } else Marshallers.bad("Invalid User information.")
    } else  Marshallers.forbidden("Capcha check failed")
  }

  get("/confirm") {
    val confirmCode = params.getOrElse("code","")

    val result = usersService.confirmEmail(confirmCode)
    if (result)  Marshallers.redirect("http://localhost/signin")
    else  Marshallers.redirect("http://localhost:3000/signup")
  }

  post("/resend-email") {
    val resendEmail = parsedBody.extract[ResendEmail]

    usersService.findByEmailOrUserid(resendEmail.user) match {
      case Some(user) =>
        usersService.getEmailConfirmCode(user) match {
          case Some(code) =>
                Emailer.sendEmailConfirmation(user, code)
                Marshallers.ok()
          case None => Marshallers.bad("Confirmation code is not exist.")
        }
      case None => Marshallers.bad("User is not exist.")
    }
  }

}

case class SignUpUser(name: String, email: String, password: String, grecaptcha: String, accountType: Int) 
case class ResendEmail(user: String)