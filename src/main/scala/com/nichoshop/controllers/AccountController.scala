package com.nichoshop.controllers

import com.nichoshop.services.Services
import com.nichoshop.utils.{AuthenticationException, UserNotFoundException, Marshallers, Emailer, SecureUtils, Constants}
import org.json4s.JsonAST._
import com.nichoshop.model.dto.AddressDto
import com.nichoshop.models.AddressEntity

/*
    Created by Nursultan 5/18/2022
*/

class AccountController extends CustomerController {

    override def name: String = "user/account"

    val usersService = Services.usersService
    val addressService = Services.addressService
    val auth = Services.authService

    get("/") {
        auth.withUser { u =>() =>
            
            usersService.findByEmailOrUserid(u.getUserid) match {
             case Some(user) =>
   
                 val businessNAddress = usersService.findBusinessById(user.businessId)
                 val address = addressService.findById(user.registrationAddressId)
               
               JObject(
                   List(
                       Some("success" -> JBool(value = true)),
                       Some("accountType" -> JInt(user.accountType)),
                       Some("userid" -> JString(user.userid)),
                       Some("email" -> JString(user.email)),
                       Some("password" -> JString(user.password)),
                       Some("phone" -> JString(user.phone.getOrElse(""))),
                       if (address.nonEmpty)  Some("address" -> Marshallers.toJson(address.get))
                       else None,
                       if( user.accountType == Constants.AccountType.business)
                           Some("business" -> Marshallers.toJson(businessNAddress))
                       else None
                   ).flatten
               )
   
             case None => throw new UserNotFoundException()
            }
        }
    }

    put("/changeType"){
        val c = parsedBody.extract[ChangeType]
        
        auth.withUser { user =>() =>
            
            usersService.changeAccountType(c.userid, c.accountType)
            Marshallers.ok()
        }
                
    }

    put("/changeUsername"){
        val c = parsedBody.extract[ChangeUsername]
        
        auth.withUser { user =>() =>
            
            usersService.changeUsername(c.userid, c.newUsername) match {
                case Some(u) =>
                    Emailer.sendUsernameChange(u,c.userid)
                    Marshallers.ok()
                case None => Marshallers.bad("Cannot change username.")
            }
        }
    }

    put("/changeEmail") {
        val c = parsedBody.extract[ChangeEmail]
        
        auth.withUser { user =>() =>
            
            usersService.changeEmail(c.userid, c.newEmail) match {
                case Some(u) => 
                        Marshallers.ok()
                case None => Marshallers.bad("Cannot change email.")
            }
        }
    }

    put("/changePassword") {
        val c = parsedBody.extract[ChangePassword]
        
        auth.withUser { user =>() =>
            
            if(c.newPassword == c.confirmPassword){
                usersService.changePassword(c.userid, c.newPassword) match {
                    case Some(u) =>
                        Emailer.sendPasswordChange(u,c.newPassword)
                        Marshallers.ok()
                    case None => Marshallers.bad("Cannot change password.")
                }
            } else Marshallers.bad("Password confirmation failed.")
        }
    }

    put("/changeAddress") {
        val c = parsedBody.extract[AddressClass]
        
        auth.withUser { user =>() =>
            
            usersService.findByEmailOrUserid(user.getUserid) match {
                case Some(u) =>
                    addressService.changeAddress(u.registrationAddressId, c.address1, c.address2, c.city, c.state, c.zip, c.country, c.phone, c.name)
                    Marshallers.ok()
                case None => Marshallers.bad("Cannot find user.")
            }
        }
                
    }
    
    put("/changePhone") {
        val c = parsedBody.extract[ChangePhone]
        
        auth.withUser { user =>() =>
            
            usersService.changePhone(c.userid, c.newPhone) match {
                case Some(u) => 
                    Marshallers.ok()
                case None => Marshallers.bad("Cannot change email.")
            }
        }

    }

    post("/confirm-suc"){

        val sucConfirm = parsedBody.extract[SUCConfirm]

        usersService.findByEmailOrUserid(sucConfirm.userid) match {
        case Some(user) =>
            
            Services.sucService.findLastOne(user.id.get, Constants.sucType.account) match {
            case Some(sucData)=> 
                
                if(sucData.code == sucConfirm.suc){

                    if((System.currentTimeMillis() - sucData.created) <= Constants.sucExpire.account){
                        Marshallers.ok()
                    } else Marshallers.bad("SUC code expired.")
                
                } else Marshallers.bad("SUC code invalid.")

            case None => Marshallers.bad("Unable to find SUC")
            }
        case None => throw new UserNotFoundException()
        }
    
    }

    post("/send_suc"){
        val sucRequest = parsedBody.extract[SUCRequest]

        usersService.findByEmailOrUserid(sucRequest.userid) match {
            case Some(user) => {

                val usedSUCList = Services.sucService.getSUCsByUserId(user.id.get)

                val suc = SecureUtils.generateSUC(usedSUCList)

                Services.sucService.createSUC(user.id.get, suc, Constants.sucType.account)

                if(suc.length>0){
                    Emailer.sendSUCConfirmation(user,suc)
                    Marshallers.ok()
                } else Marshallers.bad("Unable to create SUC")
            }
            case None => throw new UserNotFoundException()
        }
    }

    post("/closeAccount") {

    }

    post("/business-details"){
        val businessClass = parsedBody.extract[BusinessClass]
        
        auth.withUser { user=>() =>
            usersService.addBusinessData(
                user.getId, 
                businessClass.address.toAddressEntity(user.getId),
                businessClass.name,
                businessClass.vatCountry,
                businessClass.vatNumber
            )
            Marshallers.ok()
        }
    }
}

case class ChangeType(userid: String, accountType: Int)
case class ChangeUsername(userid: String, newUsername: String)
case class ChangeEmail(userid: String, newEmail: String)
case class ChangePassword(userid: String, currentPassword: String, newPassword: String, confirmPassword: String)
case class AddressClass( address1: String, address2: Option[String] = None, city: String, state: Option[String], zip: String, country: String, phone: String, name: String){
    def toAddressEntity(userId: Int) : AddressEntity = AddressEntity(
            userId = userId,
            address1 = address1,
            address2 = address2,
            city = city,
            state = state, //2 letters
            zip = zip,
            country = country,//ISO country code (2 letters)
            phone = phone,
            name = name
    )
}
case class ChangePhone(userid: String, newPhone: Option[String])
case class SUCConfirm(userid: String, suc: String)
case class SUCRequest(userid: String)
case class BusinessClass(name: String, address: AddressClass, vatCountry: String, vatNumber: String ){
}