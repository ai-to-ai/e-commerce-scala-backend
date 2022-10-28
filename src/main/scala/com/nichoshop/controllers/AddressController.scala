package com.nichoshop.controllers

import com.nichoshop.utils.Marshallers
import com.nichoshop.services.Services
import com.nichoshop.models.AddressEntity
import org.json4s.JsonAST.JArray
import java.util.Optional

/*
    Created by Nursultan 5/11/2022
*/

class AddressController extends AuthController {

  override def name: String = "user/address"

  val auth = Services.authService
  val addressService = Services.addressService
  val usersService = Services.usersService

  // Get List of address by user.
  get("/list") {
    auth.withUser { user =>() =>

     val addresses = addressService.findByUserId(user.getId)
      Marshallers.toJson(addresses)
    }
  }

  // Create a new address
  post("/") {
    val address = parsedBody.extract[Address]
    
    auth.withUser { user =>() =>

      val adddressEntity = address.toAddressEntity(user.getId)
      val addressId = addressService.create(adddressEntity)
      usersService.addAddressToUser(user.getId,addressId)

      Marshallers.ok()
    }
  }

  // Get an Address by addressType
  get("/type") {
    val addressType = params.getOrElse("address_type","0").toInt
    auth.withUser { user =>() =>

      val addresses = addressService.findByAddressType(user.getId,addressType ) 
      Marshallers.toJson(addresses)
    }
  }

  // Create a Temp Address
  post("/temp") {
    val address = parsedBody.extract[Address]
    
    auth.withUser { user =>() =>

      val adddressEntity = address.toAddressEntity(user.getId)
      addressService.create(adddressEntity)

      Marshallers.ok()
    }
  }

  // Update an address by id
  put("/update") {
    val id = params.getOrElse("id","0").toInt
    val address = parsedBody.extract[Address]
    
    auth.withUser { user =>() =>
      
      val adddressEntity = address.toAddressEntity(user.getId)
      addressService.updateById(Some(id),adddressEntity)
      Marshallers.ok()
    }
  }

  // Update a status
  put("/update-status") {

    val id = params.getOrElse("id","0").toInt
    val status = params.getOrElse("status","0").toInt

    auth.withUser { user =>() =>

      addressService.updateStatus(id, status)
      Marshallers.ok()
    }
  }

  // Delete an address by id
  delete("/") {
    val id = params.getOrElse("id","0").toInt

    auth.withUser { user =>() =>

      addressService.deleteById(id)
      Marshallers.ok()
    }    
  }

}
  case class Address(
              address1: String, 
              address2: Option[String] = None, 
              city: String, 
              state: Option[String], 
              zip: String, 
              country: String, 
              phone: String ="", 
              addressType: Int = 0, 
              status: Int = 0, 
              name: String) {
    
    def toAddressEntity(userId : Int): AddressEntity =  AddressEntity(
            userId = userId,
            name = name,
            address1 = address1,
            address2 = address2,
            city = city,
            state = state,
            zip = zip,
            country = country,
            phone = phone,
            addressType = addressType,
            status = status
          )
  
  }