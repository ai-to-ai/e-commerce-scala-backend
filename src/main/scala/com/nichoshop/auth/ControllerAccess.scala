package com.nichoshop.auth

/**
 * Created by Evgeny Zhoga on 23.08.15.
 */
object ControllerAccess {
  type ControllerAccessType = ControllerAccess
  val SELLER = new ControllerAccess() {
    def allowed(that: ControllerAccess): Boolean = true
  }
  val CUSTOMER = new ControllerAccess() {
    def allowed(that: ControllerAccess): Boolean = this == that
  }
}

private[auth] trait ControllerAccess {
  def execute[T](requiredLevel: ControllerAccess)(handler: => T)(implicit notAuthorized: () => Nothing): T =
    if (this.allowed(requiredLevel)) handler
    else notAuthorized()

  def allowed(access: ControllerAccess): Boolean
}
