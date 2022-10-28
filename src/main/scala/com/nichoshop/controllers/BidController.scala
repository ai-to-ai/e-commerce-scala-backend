package com.nichoshop.controllers

import com.nichoshop.swaggers.BidOperations
import org.scalatra.swagger.Swagger
import com.nichoshop.models.helpers._
import com.nichoshop.utils
import com.nichoshop.models.{Bids, BidEntity}

import com.nichoshop.services.Services

/**
 * Created by Nursultan on 6/7/2022
 */

class BidController(implicit val swagger: Swagger) extends AuthController with BidOperations with utils.Pagination {

    val bidService = Services.bidService

    get("/", operation(getList)){
        bidService.getList(uid)
    }

    post("/", operation(addBid)){
        val bidBody= parsedBody.extract[BidBody]
        val newBid = new BidEntity(None, bidBody.itemId, uid, bid =RichMoney(bidBody.amount, bidBody.code))
        if(bidService.isAuction(bidBody.itemId)){
            bidService.addBid(newBid)
        } else throw new Exception("Not a auction listing.")
    }

    put("/", operation(cancelBid)){
        val id = params.getOrElse("id","0").toInt
        if(bidService.isCancellable(id)){
            bidService.cancelBid(id)
        } else throw new Exception("Cannot cancel this bid.")
    }

    get("/count", operation(getBidCount)) {
        bidService.getBidCount(uid)
    }
}

case class BidBody(itemId: Int, amount: Int, code: Int)