package com.nichoshop.controllers

import com.nichoshop.services.Services
import com.nichoshop.swaggers.CreditCardOperations
import com.nichoshop.models.{CreditCardEntity, CreditCards }
import org.scalatra.swagger.Swagger

class CreditCardController(implicit val swagger: Swagger) extends AuthController with CreditCardOperations {

    val creditCardService = Services.creditCardService

    get("/"){
        creditCardService.getList(uid)
    }

    post("/"){
        val creditCardBody = parsedBody.extract[CreditCardBody]
        val creditCardEntity = new CreditCardEntity(
            userId = uid,
            holder = creditCardBody.holder,
            cardType = creditCardBody.cardType,
            number = creditCardBody.number,
            month = creditCardBody.month,
            year = creditCardBody.year,
            code = creditCardBody.code,
            addressId = creditCardBody.addressId,
            status = creditCardBody.status
        )
        if(creditCardService.isValid(creditCardEntity))
            creditCardService.createCard(creditCardEntity)
        else throw new Exception("Card is not valid")
    }

    delete("/"){
        val id = params.getOrElse("id","0").toInt
        creditCardService.deleteCard(id)
    }

    put("/") {
        val creditCardBody = parsedBody.extract[CreditCardBody]
        val creditCardEntity = new CreditCardEntity(
            userId = uid,
            holder = creditCardBody.holder,
            cardType = creditCardBody.cardType,
            number = creditCardBody.number,
            month = creditCardBody.month,
            year = creditCardBody.year,
            code = creditCardBody.code,
            addressId = creditCardBody.addressId,
            status = creditCardBody.status
        )
        if(creditCardService.isValid(creditCardEntity))
            creditCardService.updateCard(creditCardEntity)
        else throw new Exception("Card is not valid")
    }
}

case class CreditCardBody(
            holder: String, 
            cardType: Int, 
            number: Long, 
            month: Int, 
            year: Int, 
            code: String,
            addressId: Int,
            status: Boolean = false
)