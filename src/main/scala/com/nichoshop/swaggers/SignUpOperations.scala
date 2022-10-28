package com.nichoshop.swaggers

import com.nichoshop.controllers.SignUpUser
import org.scalatra.swagger.SwaggerSupport


trait SignUpOperations extends SwaggerSupport {

  def name: String = "Signup"

  protected def applicationDescription = s"Operations on sign up"

  val signUp = (apiOperation[Unit]("sign_up")
    summary s"Sign up"
    parameter bodyParam[SignUpUser]("user").description("new user")
    )

  val confirmEmail = (apiOperation[Int]("confirm_email")
    summary s"Confirm email"
    notes "1 if confirmation passed, 0 if had already passed before or didn't pass"
    parameter pathParam[String]("code").description("Generated code to confirm email")
    )

  val checkEmail = (apiOperation[Int]("check_email")
    summary s"Check whether such email already exists"
    notes "Returns 1 if such email already exists else returns 0"
    parameter pathParam[String]("email").description("email")
    )

  val checkUid = (apiOperation[Int]("check_userid")
    summary s"Check whether such userid already exists"
    notes "Returns 1 if such userid already exists else returns 0"
    parameter pathParam[String]("userid").description("userid")
    )

}
