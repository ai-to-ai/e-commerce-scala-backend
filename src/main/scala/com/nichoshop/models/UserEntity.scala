package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.model.dto.AccountType
import com.nichoshop.models.helpers._
import org.mindrot.jbcrypt.BCrypt

import scala.slick.lifted.{Index, Tag}

/**
 * Created by Evgeny Zhoga on 13.06.15.
 */
case class UserEntity(
            override val id: Option[Int] = None,
            userid: String,
            password: String,
            name: String,
            lname: String,
            email: String,
            emailConfirmed: Boolean = false,
            suspended: Boolean = false,
            deleted: Boolean = false,
            deletedDate: Option[Long] = None,
            question: Option[String] = None,
            answer: Option[String] = None,
            registrationAddressId: Option[Int] = None,
            fromAddressId: Option[Int] = None,
            toAddressId: Option[Int] = None,
            returnAddressId: Option[Int] = None,
            paymentAddressId: Option[Int] = None,
            phone: Option[String] = None,
            accountType: Int = 0,
            businessId: Option[Int] = None,
            phoneConfirmed: Boolean = false,
            registrationDate: Long = System.currentTimeMillis()

) extends IdAsPrimaryKey

class Users(tag: Tag) extends TableWithIdAsPrimaryKey[UserEntity](tag, "users") {

  def userid = column[String]("userid")

  def password = column[String]("password")

  def email = column[String]("email")

  def name = column[String]("name")

  def lname = column[String]("lname")

  def registrationDate = column[Long]("registration_date")

  def emailConfirmed = column[Boolean]("email_confirmed")

  def suspended = column[Boolean]("suspended")

  def deleted = column[Boolean]("deleted")

  def deletedDate = column[Option[Long]]("deleted_date")

  def question = column[Option[String]]("question")

  def answer = column[Option[String]]("answer")

  def registrationAddressId = column[Option[Int]]("registration_address_id")

  def fromAddressId = column[Option[Int]]("from_address_id")

  def toAddressId = column[Option[Int]]("to_address_id")

  def returnAddressId = column[Option[Int]]("return_address_id")

  def paymentAddressId = column[Option[Int]]("payment_address_id")

  def phone = column[Option[String]]("phone")

  def accountType: Column[Int] = column[Int]("account_type")
  
  def businessId = column[Option[Int]]("business_id")

  def phoneConfirmed = column[Boolean]("phone_confirmed")

  def uniqueEmailIndex: Index = index("unique_email", email, unique = true)

  def uniqueUseridIndex: Index = index("unique_userid", userid, unique = true)

  def * = (id.?, userid, password, email, name, lname, registrationDate,
    emailConfirmed, suspended, deleted, deletedDate, question,
    answer, registrationAddressId, fromAddressId, toAddressId, returnAddressId, paymentAddressId,
    phone, accountType, businessId, phoneConfirmed) <> (UserEntity.tupled, UserEntity.unapply)
}

object Users extends QueryForTableWithIdAsPrimaryKey[UserEntity, Users](TableQuery[Users]) {
  def findByEmailOrUserid(emailOrUserid: String): Option[UserEntity] = DB.read({ implicit s =>
    query.filter(u => u.email === emailOrUserid || u.userid === emailOrUserid).firstOption
  })

  def findByEmail(email: String): Option[UserEntity] = DB.read({ implicit s =>
    query.filter(_.email === email).firstOption
  })

  def findByUserid(userid: String): Option[UserEntity] = DB.read({ implicit s =>
    query.filter(_.userid === userid).firstOption
  })

  def findByPhone(phone: String): Option[UserEntity] = DB.read({ implicit s =>
    query.filter(_.phone === phone).firstOption
  })

  def encryptPassword(plain: String): String = BCrypt.hashpw(plain, BCrypt.gensalt())
}
