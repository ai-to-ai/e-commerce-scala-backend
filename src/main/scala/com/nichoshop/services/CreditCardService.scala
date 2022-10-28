package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers.DB
import com.nichoshop.models.{CreditCardEntity, CreditCards, Addresses}
import com.nichoshop.utils.SecureUtils

import java.util.Calendar

import org.slf4j
import org.slf4j.LoggerFactory

class CreditCardService {
    
    val creditCards = CreditCards.query
    val addresses = Addresses.query

    def createCard(x: CreditCardEntity) = DB.write { implicit session =>
        creditCards.insert(x)
    }

    def isValid(x: CreditCardEntity) : Boolean = {
        var result = false
        val thisYear = SecureUtils.year2time(Calendar.getInstance.get(Calendar.YEAR))
        val thisMonth = SecureUtils.year2time(Calendar.getInstance.get(Calendar.MONTH))

        val address = DB.read { implicit session =>
            addresses.filter(_.id === x.addressId).list.headOption
        }
        if(address.isDefined){
            if(x.code.length > 2 && x.code.length <5){
                if(x.year >= thisYear){
                    if(x.year == thisYear){
                        if(x.month >= thisMonth){
                            result = true
                        }
                    } else result = true
                }
            }
        }

        result
    }

    def updateCard(x: CreditCardEntity) : Unit = DB.write { implicit session =>
        x.id match {
            case Some(id) => creditCards.filter(_.id === x.id).update(x)
            case None => new Exception("Cannot update credit card")
        }
        
    }
    def getList(userId: Int) = DB.read { implicit session =>
        creditCards.filter(_.userId === userId).list    
    }

    def deleteCard(id: Int) = DB.write { implicit session =>
        creditCards.filter(_.id === id).delete    
    }
}