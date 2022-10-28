package com.nichoshop.utils

import com.nichoshop.Environment
import com.nichoshop.models.{PasswordResetEntity, UserEntity}
import org.slf4j
import org.slf4j.LoggerFactory

object Emailer {
  import Environment._
  def sendEmailConfirmation(user: UserEntity, code: String) = {
    //TODO
    val message =
      s"""
        |Hello ${user.name} ${user.lname},
        |
        |This email address was used to register on www.nichoshop.com
        |To confirm this address, please, click on link:
        |
        |If you did not register on $host, ignore this message.
        |
        |$hostWithRootPath/signup/confirm?code=$code
        |
        |With best regards,
        |NichoShop Team
      """.stripMargin
    
    EmailerBase.send(user.email, "Nichoshop Account Confirmation", message = message)
  }

  def sendPasswordReset(user: UserEntity, passwordReset: PasswordResetEntity) = {
    val message =
      s"""
         |Hello ${user.name} ${user.lname},
         |
         |password reset was requested for this email.
         |If you did not ask for that, please, ignore this message. Otherwise
         |use link below to reset your password
         |
         |$hostWithRootPath/#/reset?code=${passwordReset.hash.get}
         |
         |With best regards,
         |NichoShop Team
       """.stripMargin
    EmailerBase.send(user.email, "nichoshop.com reset password", message = message)
  }

  def sendSUCConfirmation(user: UserEntity, suc: String) = {
    val message = 
      s"""
        |Hello ${user.name} ${user.lname},
        |
        |If you forgot password, please use this code.
        |It can be used once and never again.
        |
        |SUC = ${suc}
        |
        |With best regards,
        |NichoShop Team
      """.stripMargin
      EmailerBase.send(user.email,"nichoshop.com suc confirmation", message = message)
  }

  def sendUsernameChange(user: UserEntity,prevUsername: String) = {
    val message =
      s"""
         |Hello ${user.name} ${user.lname},
         |
         |Your username is renamed from ${prevUsername} to ${user.userid}.
         |
         |With best regards,
         |NichoShop Team
       """.stripMargin
    EmailerBase.send(user.email, "nichoshop.com reset username", message = message)
  }
  def sendPasswordChange(user: UserEntity,password: String) = {
    val message =
      s"""
         |Hello ${user.name} ${user.lname},
         |
         |Your password was changed to ${password}.
         |Please remember this password.
         |
         |With best regards,
         |NichoShop Team
       """.stripMargin
    EmailerBase.send(user.email, "nichoshop.com reset username", message = message)
  }

}
