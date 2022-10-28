package com.nichoshop.services

import com.nichoshop.model.dto._

import scala.collection.JavaConversions._

/**
 * Created by Evgeny Zhoga on 29.11.15.
 */
class PermissionsService {
  def withProtection[T](permission: Permission)(user: UserDto)(f: => T): Option[T] = {
    if (
      (permission.allowed(Role.USER) && user.getRoles.contains(Role.USER)) ||
        (permission.allowed(Role.ADMIN) && user.getRoles.contains(Role.ADMIN)) ||
        (permission.allowed(Role.SUPPORT))
        // (permission.allowed(Role.SUPPORT) && user.getPermissions.toList.exists(_.getCode == permission.name))
    ) Some(f)
    else None
  }
}

sealed trait Permission {
  def name: String

  def description: String

  def allowed(role: Role): Boolean
}

object PermissionsService {
  val customers = List(Role.USER)
  val support = List(Role.SUPPORT)
  val admin = List(Role.ADMIN)
  val all = customers ::: support ::: admin
  val admins = admin ::: support

  private def p(n: String, d: String, roles: List[Role]) = {
    val p = new Permission {
      val name = n
      val description = d

      def allowed(role: Role) = roles.contains(role)
    }
    Permissions._m += p.name -> p
    p
  }

  object Permissions {
    private[PermissionsService] val _m = scala.collection.mutable.HashMap.empty[String, Permission]

    val `tpermission:add` = p("tpermission-add", "Protect 'Add permission type' handler", admin)
    val `tpermission:remove` = p("tpermission-remove", "Protect 'Remove permission type' handler", admin)
    val `permission:add` = p("permission-add", "Protect 'Add permission' handler", admin)
    val `permission:remove` = p("permission-remove", "Protect 'Remove permission' handler", admin)
    val `users:get` = p("users-get", "Protect 'Get users list' handler", admin)
    val `user:get` = p("user-get", "Protect 'Get user' handler", admin)

    val `categories:add` = p("categories-add", "Protect 'Add category' handler", support)
    val `category:move` = p("category-move", "Protect 'Move category to another parent' handler", support)

    val `admin_category:add` = p("admin_categories-add", "Protect 'Add category' handler", admin)
    val `admin_category:remove` = p("admin_categories-remove", "Protect 'Remove category' handler", admin)
    val `admin_category:rename` = p("admin_categories-rename", "Protect 'Rename category' handler", admin)
    val `admin_category:move` = p("admin_categories-move", "Protect 'Move category' handler", admin)

    val map = _m.toMap
  }
}
