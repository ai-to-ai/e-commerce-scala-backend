package com.nichoshop.controllers

import com.nichoshop.services.Services
import com.nichoshop.swaggers.SellerOrderOperations
import org.scalatra.swagger.Swagger
import com.nichoshop.utils.{Pagination, Marshallers, Constants}
import com.nichoshop.models.MessageEntity

class SellerOrderController(implicit val swagger: Swagger) extends AuthController with SellerOrderOperations with Pagination{

    val sellOrderService = Services.sellOrderService
    val messageService = Services.messageService
    
    get("/"){
        val sOrderFilter = parsedBody.extract[SellerOrderFilter]
        sellOrderService.getList(uid,sOrderFilter)
    }

    get("/detail", operation(getDetail)){
        val id = params.getOrElse("id","0").toInt
        sellOrderService.getDetail(id)
    }

    get("/orderId", operation(getDetail)){
        val id = params.getOrElse("id","0").toInt
        sellOrderService.getDetail(id)
    }

    post("/confirm-shipment", operation(confirmShip)){
        val id = params.getOrElse("id","0").toInt
        sellOrderService.confirmShipment(id)
    }

    post("/contact-buyer", operation(contactBuyer)){
        val messageBody = parsedBody.extract[MessageBody]
        val m = new MessageEntity(
            fromId = uid,
            toId = messageBody.toId,
            message = messageBody.message,
            messageType = Constants.MessageType.contactBuyer
        )
        messageService.create(m)
    }

    post("/report-buyer", operation(reportBuyer)){
        val messageBody = parsedBody.extract[MessageBody]
        val m = MessageEntity(
            fromId = uid,
            toId = messageBody.toId,
            message = messageBody.message,
            reason = messageBody.reason,
            messageType = Constants.MessageType.reportBuyer
        )
        messageService.create(m)
    }

    post("/refund", operation(refund)){
        val messageBody = parsedBody.extract[MessageBody]
        val m = MessageEntity(
            fromId = uid,
            toId = messageBody.toId,
            message = messageBody.message,
            reason = messageBody.reason,
            messageType = Constants.MessageType.refundOrder
        )
        messageService.create(m)


    }
    post("/cancel-item", operation(cancelItem)){
        val messageBody = parsedBody.extract[MessageBody]
        val m = MessageEntity(
            fromId = uid,
            toId = messageBody.toId,
            message = messageBody.message,
            reason = messageBody.reason,
            messageType = Constants.MessageType.cancelItem
        )
        messageService.create(m)

    }
    
    get("/cancel"){
        val cancelFilter = parsedBody.extract[CancelFilter]
    }
}

case class SellerOrderFilter (
    status: Int = 0, 
    duration: Int = 0, 
    searchKey: Int = 0, 
    searchWord: String = "", 
    sort: Int = 0
)

case class CancelFilter (
    status: Int = 0,
    duration: Int = 0,
    searchKey: Int
)

case class MessageBody (
    toId : Int,
    message: String = "",
    reason: Option[String] = None
)
