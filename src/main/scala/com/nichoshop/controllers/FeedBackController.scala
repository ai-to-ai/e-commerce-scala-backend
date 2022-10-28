package com.nichoshop.controllers

import com.nichoshop.swaggers.FeedBackOperations
import org.scalatra.swagger.Swagger
import com.nichoshop.services.Services
import com.nichoshop.models.{FeedBackEntity, FeedBacks}

class FeedBackController(implicit val swagger: Swagger) extends AuthController with FeedBackOperations {

    val feedBackService = Services.feedBackService

    get("/"){
        feedBackService.getList(uid)
    }

    post("/"){
        val feedBackBody = parsedBody.extract[FeedBackBody]
        val feedBackEntity = new FeedBackEntity(
                                id = None,
                                userId= uid,
                                sellerId = feedBackBody.sellerId,
                                sellId = feedBackBody.sellId,
                                rating = feedBackBody.rating,
                                itemAsDescribed = feedBackBody.itemAsDescribed,
                                communication = feedBackBody.communication,
                                shippingTime = feedBackBody.shippingTime,
                                shippingCharges = feedBackBody.shippingCharges,
                                message = feedBackBody.message,
                            )
        feedBackService.create(feedBackEntity)
    }
}

case class FeedBackBody(
            sellerId: Int, 
            sellId: Int, 
            rating: Int, 
            itemAsDescribed: Int, 
            communication: Int, 
            shippingTime: Int, 
            shippingCharges: Int, 
            message: Option[String] = None
)