package com.nichoshop.controllers

import com.nichoshop.swaggers.OrderOperations
import org.scalatra.swagger.Swagger
import com.nichoshop.services.Services
import com.nichoshop.utils.{Pagination, Marshallers}
import com.nichoshop.models.OrderEntity
import com.nichoshop.models.helpers.RichMoney

class OrderController(implicit val swagger: Swagger) extends AuthController with OrderOperations with Pagination {

    val orderService = Services.orderService

    get("/", operation(getList)){
        val orderFilter = new OrderFilter(params.getOrElse("duration","0").toInt, params.getOrElse("status","0").toInt)
        // Marshallers.toJson(orderService.getList(uid, orderFilter))
    }

    get("/details", operation(getDetails)){
        val id = params.getOrElse("id","0").toInt
        // Marshallers.toJson(orderService.getDetails(id))
    }

    post("/", operation(createOrder)){
        val orderBody = parsedBody.extract[OrderBody]
        val orderEntity = new OrderEntity(
            id = None,
            buyerId = uid,
            sellerId = orderBody.sellerId,
            delivery = orderBody.delivery,
            trackingId = orderBody.trackingId,
            orderId = orderBody.orderId,
            items = orderBody.items,
            status = orderBody.status,
            payment = orderBody.payment,
            total = new RichMoney(0)
        )
        orderService.createOrder(orderEntity)
    }

    delete("/", operation(deleteOrder)){
        val id = params.getOrElse("id","0").toInt
        orderService.deleteOrder(id)
    }


}

case class OrderFilter(duration: Int, status: Int)
case class OrderBody(
            sellerId: Int,
            payment: Int,
            delivery: Long,
            trackingId: Int,
            orderId: String,
            items: List[String],
            status: Int = 0

)