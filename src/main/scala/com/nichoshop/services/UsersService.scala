package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.model.dto._
import com.nichoshop.models.helpers.DB
import com.nichoshop.models.{
        Addresses, 
        Users, 
        Roles,
        Businesses, 
        EmailConfirms, 
        RoleEntity, 
        BusinessEntity, 
        UserEntity, 
        EmailConfirmEntity,
        AddressEntity
      }
import com.nichoshop.utils.Converters._
import com.nichoshop.utils.Constants
import com.nichoshop.utils.Marshallers
import scala.concurrent.duration._

import org.mindrot.jbcrypt.BCrypt

/**
 * Modified by Nursultan on 7/3/2022.
 * 
 */
class UsersService {

  val addresses = Addresses.query
  val users = Users.query
  val businesses = Businesses.query
  val roles = Roles.query
  val emailConfirms = EmailConfirms.query

  def getUserAddresses(userId: Int): List[AddressDto] = DB.read { implicit session =>
    addresses.filter(_.userId === userId).list.map(v => v: AddressDto)
  }

  def addPermissionToUser(userId: Int, role: Role) = {
    // DB.write { implicit session =>
    //   Roles.insert(
    //     RoleEntity(id = None, userId = userId, getPermissionType(role))
    //   )
    // }
  }

  def removePermissionFromUser(userId: Int, permission: Role) = {
    // DB.write { implicit session =>
    //   roles.filter(
    //     p => (p.permissionType === getPermissionType(permission)) && (p.userId === userId)
    //   ).delete
    // }
  }

  def hasAdminOrCustomerSupportRole(user: UserDto): Boolean = {
    hasAdminRole(user) || hasCustomerSupportRole(user)
  }

  def hasCustomerSupportRole(user: UserDto): Boolean = {
    hasRole(user, Role.SUPPORT)
  }

  def hasAdminRole(user: UserDto): Boolean = {
    hasRole(user, Role.ADMIN)
  }

  private def hasRole(user: UserDto, tPermission: Role): Boolean = {
    user.getRoles.contains(tPermission)
  }

  private def isCustomerSupport(userId: Int) = getUser(userId).exists(_.getRoles.contains(Role.SUPPORT))

  def addPermissionToUser(userId: Int, permissionCode: String) = {
    // DB.write { implicit session =>

    //   if (isCustomerSupport(userId)) {
    //     PermissionsService.Permissions.map.get(permissionCode).foreach { p =>
    //       models.Permissions.insert(
    //         models.PermissionEntity(id = None, userId = userId, p.name)
    //       )
    //     }
    //     true
    //   } else {
    //     false
    //   }
    // }
  }

  def removePermissionFromUser(userId: Int, permissionCode: String) = {
    // DB.write { implicit session =>
    //   PermissionsService.Permissions.map.get(permissionCode).foreach { p =>
    //     models.Permissions.query.filter(
    //       p1 => (p1.code === p.name) && (p1.userId === userId)
    //     ).delete
    //   }
    // }
  }

  // TODO: vnavozenko: we need to refactor this method(create service for admin usage)
  def addUser(user: UserDto, nonEncryptedPassword: String, permissions: List[String]) = {
    DB.write { implicit session =>
      val hashed = BCrypt.hashpw(nonEncryptedPassword, BCrypt.gensalt())

      val u: UserEntity = UserEntity(
        userid = user.getUserid,
        password = hashed,
        email = user.getEmail,
        emailConfirmed = user.getEmailConfirmed,
        lname = user.getLastName,
        name = user.getFirstName,
        accountType = Constants.AccountType.system
      )

      val id = Users.insert(u)
      //      val createdUser = u.copy(id = Some(id))

      addPermissionToUser(id, Role.SUPPORT)
      permissions.foreach { permission =>
        addPermissionToUser(id, permission)
      }
    }
  }

  def getUser(id: Int): Option[UserDto] = {
    DB.read { implicit session =>
      Users.findById(id).map { user =>
        val rolesList = roles.filter(_.userId === id).list

        (user, rolesList): UserDto
      }
    }
  }

  def getUserList(filters: UsersService.Filters = UsersService.Filters()): List[UserDto] = {
    DB.read { implicit session =>

      val usersList: List[UserEntity] = Option(users).get.list

      val rolesList: Map[Int, List[RoleEntity]] = 
          roles.filter(_.userId inSet usersList.map(_.id.get))
                .list.groupBy(_.userId)

      usersList.map(u => (u, rolesList.getOrElse(u.id.get, List.empty[RoleEntity])): UserDto)

    }
  }

  def getCustomerSupports(filters: UsersService.Filters = UsersService.Filters()): List[UserDto] = {
    DB.read { implicit session =>

      val usersList: List[UserEntity] = Option(for {
        user <- users
        role <- roles
        if (user.id === role.userId) && (role.permissionType === Role.SUPPORT.name())
      } yield user).get.list

      val rolesList: Map[Int, List[RoleEntity]] = 
          roles.filter(_.userId inSet usersList.map(_.id.get))
                .list.groupBy(_.userId)

      usersList.map(u => (u, rolesList.getOrElse(u.id.get, List.empty[RoleEntity])): UserDto)
    }
  }

  private def getRoleType(role: Role): String = {
    role match {
      case Role.ADMIN => Roles.Type.admin
      case Role.SUPPORT => Roles.Type.support
      case Role.USER => Roles.Type.user
    }
  }

  def findByEmailOrUserid(emailOrUserid: String) = Users.findByEmailOrUserid(emailOrUserid)

 

  def getUserAccount(userid: String) = DB.write {implicit s=>
    val account = Option(for {
      user <- users
      address <- addresses
      if(user.userid === userid) && (user.registrationAddressId === address.id)
    } yield (user, address))
    .get.list
    account
  }

  def findBusinessById(id: Option[Int]) = DB.read { implicit s =>
    businesses.filter(_.id === id).run.headOption match {
      case Some(b) => addresses.filter(_.id === b.addressId).run.headOption match {
        case Some(a) => (b: BusinessDto, a: AddressDto)
      }
    }
  }

  def changeAccountType(userid: String, accountType: Int) = DB.write {implicit s =>
    users.filter(_.userid === userid).map(_.accountType).update(accountType)
  }
  def changeUsername(userid: String, newUsername: String) : Option[UserEntity] = DB.write {implicit s =>

      users.filter(_.userid === userid).map(_.userid).update(newUsername)
      users.filter(_.userid === newUsername).run.headOption
  }

  def changeEmail(userid: String, newEmail: String) : Option[UserEntity] = DB.write {implicit s =>
      users.filter(_.userid === userid).map(_.email).update(newEmail)
      users.filter(_.userid === userid).run.headOption
  }

  def changePassword(userid: String, newPassword: String) : Option[UserEntity] = DB.write {implicit s =>
      users.filter(_.userid === userid).map(_.password).update(newPassword)
      users.filter(_.userid === userid).run.headOption
  }

  def changePhone(userid: String, newPhone : Option[String]) : Option[UserEntity] = DB.write {implicit s=>
      users.filter(_.userid === userid).map(_.phone).update(newPhone)
      users.filter(_.userid === userid).run.headOption
  }

  def confirmPhoneById(id: Option[Int]) = DB.write { implicit session =>
    users.filter(_.id === id)
        .map(_.phoneConfirmed)
        .update(true)
  }

  def checkUseridAlreadyExists(userid: String): Boolean =  DB.read { implicit session =>
    users.filter(_.userid === userid).run.headOption.isDefined
  }

  def checkEmailAlreadyExists(email: String): Boolean =  DB.read { implicit session =>
    users.filter(_.email === email).run.headOption.isDefined
  }

  def checkPhoneAlreadyExist(phone: String): Boolean =  DB.read { implicit session =>
    users.filter(_.phoneConfirmed === true).filter(_.phone === phone).run.headOption.isDefined
  }

  def addPhone(userId: String, phone: String): Unit =  DB.write { implicit session =>
    
    if (checkPhoneAlreadyExist(phone)) Marshallers.bad(s"User with phone ${phone} already exist")
    
    users.filter(_.userid === userId).filter(_.phoneConfirmed === false).map(u =>(u.phone)).update(Some(phone))
  }

  def saveEmailConfirm(x: EmailConfirmEntity) =DB.write { implicit session =>
    EmailConfirms.insert(x)
  }

  def create(user: UserEntity) = DB.write { implicit session =>
    val hashed = BCrypt.hashpw(user.password, BCrypt.gensalt())

    Users.insert(user.copy(password = hashed))
  }

  def confirmEmail(code: String) = DB.read { implicit session =>
    emailConfirms.filter(_.code === code).run.headOption match {
      case Some(e) if ((System.currentTimeMillis() - e.created) <= (2 day).toMillis) => true
      case _ => false
    }
  }

  def getEmailConfirmCode(user: UserEntity) = DB.write { implicit session =>
    emailConfirms.filter(_.userId === user.id).map(_.created).update(System.currentTimeMillis)
      emailConfirms.filter(_.userId === user.id).run.headOption.map(_.code) 
  }

  def addAddressToUser(userId: Int,addressId: Option[Int]) : Unit = DB.write { implicit session => 
    users.filter(_.id === userId)
          .map(x => (x.registrationAddressId, x.fromAddressId, x.toAddressId, x.returnAddressId))
          .update(addressId,addressId,addressId,addressId)
  }
  def checkBusinessExists(userId: Int) = DB.read { implicit session =>
    users.filter(_.id === userId).map(_.businessId).run.headOption.isDefined  
  }

  def addBusinessData(userId: Int, x: AddressEntity, businessName: String, vatCountry: String, vatNumber: String) = DB.write { implicit session =>
    if(checkBusinessExists(userId)) Marshallers.bad("Business data already exists.") 

    val addressId = Addresses.insert(x)
    val businessId = Businesses.insert(BusinessEntity(
                        userId = userId, 
                        name=businessName, 
                        addressId = Some(addressId), 
                        vatCountry = vatCountry, 
                        vatNumber = vatNumber
                      ))
    users.filter(_.id === userId)
          .map(_.businessId)
          .update(Some(businessId))
  }

}



object UsersService {
  case class Filters(
                      permissionTypes: Option[List[Role]] = None
                    )
}
