package com.nichoshop.swaggers

import com.nichoshop.legacy.models.{Manifests, UsersRow}

trait UsersOperations extends ApiDescription[UsersRow] {
  def name = "User"

  implicit def manifestForT: Manifest[UsersRow] = Manifests.user

  val loggedIn = (apiOperation[UsersRow]("logged_in")
    summary s"Returns current user currently logged in"
    tags "Users"
    )

  val restore = (apiOperation[UsersRow]("restore")
    summary s"Generates main or sms or back call to provide code for user to restore password"
    tags "Users"
    )

  val resetPassword = (apiOperation[UsersRow]("reset_password")
    summary s"Reset user password"
    tags "Users"
    )

  val checkCode = (apiOperation[UsersRow]("check_code")
    summary s"Checks phone code from user and generates restore password token in case of success"
    tags "Users"
    )
}
