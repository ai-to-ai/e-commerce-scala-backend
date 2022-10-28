package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.model.dto.AccountType
import com.nichoshop.model.dto._
import com.nichoshop.models.{Addresses, AddressEntity}
import com.nichoshop.models.helpers.DB
import com.nichoshop.utils.Converters._
import com.nichoshop.models._
import org.mindrot.jbcrypt.BCrypt

/*
    Created by Nursultan 5/12/2022
*/

class AddressService{

  val addresses = Addresses.query
  val users = Users.query

  def findByUserId(userId: Int): List[AddressDto] = DB.read { implicit session =>
    addresses.filter(_.userId === userId).list.map(v => v: AddressDto)
  }

  def create(x: AddressEntity) : Option[Int] ={
    val id =  DB.write { implicit session =>
        Addresses.insert(x)
    }
    Some(id)
}

   def findByAddressType(userId: Int, addressType: Int): List[AddressDto] = DB.read { implicit session =>
    addresses.filter(_.userId === userId)
            .filter(_.addressType === addressType)
            .list.map(v => v: AddressDto)
  }

  def deleteById(id: Int) = DB.write  { implicit session =>
    addresses.filter(_.id === id).delete
  }

  def updateById(id: Option[Int], x: AddressEntity) = DB.write { implicit session =>
    addresses.filter(_.id === id).update(x.copy(id = id))
  }

  def updateStatus(id: Int, status: Int) : Unit =  DB.write { implicit session =>
    addresses.filter(_.id === id).map(_.status).update(status)
  }

  def findById(id: Option[Int]) : Option[AddressDto] = DB.read { implicit s =>
    addresses.filter(_.id === id).run.headOption.map(v => v: AddressDto)
  }

  def changeAddress(id: Option[Int], address1: String, address2: Option[String], city: String, state: Option[String], zip: String, country: String, phone: String, name: String ) = DB.write{ implicit s =>
    addresses.filter(_.id === id)
                .map(x => (x.address1, x.address2, x.city, x.state, x.zip, x.country, x.phone, x.name))
                .update(address1,address2,city,state, zip, country, phone, name)
  }

  def isShippingExists(userId: Int) : Boolean = DB.read { implicit session =>
    val result = (for{
        address <- addresses
        user <- users
        if(address.id === user.toAddressId) && (user.id === userId)
    } yield(address)
    ).list.size
    if(result > 0) true else false
  }

}