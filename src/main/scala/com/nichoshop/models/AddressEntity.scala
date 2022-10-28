package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import scala.slick.lifted.Tag

/**
 * Created by Evgeny Zhoga on 22.11.15.
 */
case class AddressEntity(

            override val id: Option[Int] = None,
            userId: Int,
            address1: String,
            address2: Option[String] = None,
            city: String,
            state: Option[String] = None, //2 letters
            zip: String,
            country: String,//ISO country code (2 letters)
            phone: String,
            status: Int = 0,
            addressType: Int = 0,
            addressIsVerified: Boolean = false,
            name: String
                      
) extends IdAsPrimaryKey

class Addresses(tag:Tag) extends TableWithIdAsPrimaryKey[AddressEntity](tag, "addresses") {

  def userId = column[Int]("user_id")
  def address1 = column[String]("address1")
  def address2 = column[Option[String]]("address2")
  def city = column[String]("city")
  def state = column[Option[String]]("state")
  def zip = column[String]("zip")
  def country = column[String]("country")
  def phone = column[String]("phone")
  def addressType = column[Int]("address_type")
  def status = column[Int]("status")
  def addressIsVerified = column[Boolean]("address_is_verified")
  def name = column[String]("name")

  def * = ( id.?, userId, address1, address2, city, state, zip, country, phone, addressType, status, addressIsVerified, name) <> ( AddressEntity.tupled, AddressEntity.unapply )
}

object Addresses extends QueryForTableWithIdAsPrimaryKey[AddressEntity, Addresses](TableQuery[Addresses]){
//   def findByUserId(userId: Int): Option[AddressesRow] = db.withSession { implicit session =>
//     query.filter(_.userId === userId).list.headOption
//   }

//   def findByAddressType(userId: Int, addressType: Int): Option[AddressesRow] = db.withSession { implicit session =>
//     query.filter(_.userId === userId).filter(_.addressType === addressType.toString).list.headOption
//   }

//   def create(x: AddressesRow) = db.withSession { implicit session =>
//     Addresses += x
//   }

//   def deleteById(id: Int) = db.withSession { implicit session =>
// //    query.filter(_.id === id).delete
//   }

//   def updateById(id: Int, x: AddressesRow) = db.withSession { implicit session =>
//     query.filter(_.id === id).update(x.copy(id = id))
//   }

//   def updateStatus(id: Int, status: Int) : Unit =  db.withSession { implicit session =>
//     query.filter(_.id === id).map(_.status).update(status)

//   }
}

