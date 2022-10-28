package com.nichoshop.swaggers

import com.nichoshop.models.{Bids, BidEntity}
import org.scalatra.swagger.SwaggerSupport
import com.nichoshop.controllers.BidBody

trait BidOperations extends SwaggerSupport {

  def name: String = "Bid"

  override protected def applicationDescription: String = "Bid"

    val getList = (apiOperation[List[BidEntity]]("getList")
        summary "Find all Bids"
    )

     val addBid = (apiOperation[Unit]("addBid")
        summary "Add a Bid"

        parameter bodyParam[BidBody].description("Parameters for Adding a bid")
    )

     val cancelBid = (apiOperation[Unit]("cancelBid")
        summary "Cancel a bid"

        parameter queryParam[Int]("id").description("Cancelable bid's id")
    )

     val getBidCount = (apiOperation[Map[String, Int]]("addBid")
        summary "Get counts of Bids"
        notes "{all:0, win:0, lose:0}"

    )

}
