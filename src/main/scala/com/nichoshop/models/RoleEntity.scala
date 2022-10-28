package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import scala.slick.lifted.Tag

/**
 * Created by Evgeny Zhoga on 03.07.15.
 */
case class RoleEntity(

            override val id: Option[Int],
            userId: Int,
            permissionType: String
            
) extends IdAsPrimaryKey

class Roles (tag:Tag) extends TableWithIdAsPrimaryKey[RoleEntity](tag, "roles") {
  def userId = column[Int]("user_id")
  def permissionType = column[String]("type")

  def * = ( id.?, userId, permissionType) <> ( RoleEntity.tupled, RoleEntity.unapply )

}

object Roles extends QueryForTableWithIdAsPrimaryKey[RoleEntity, Roles](TableQuery[Roles]) {
  object Type {
    val user = "user"
    val support = "support"
    val admin = "admin"
  }
}