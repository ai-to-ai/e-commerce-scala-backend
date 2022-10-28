package com.nichoshop.swaggers

import com.nichoshop.models.{Orders, OrderEntity}
import org.scalatra.swagger.SwaggerSupport
import com.nichoshop.controllers.SellerOrderFilter

trait SellerOrderOperations extends SwaggerSupport {

  def name: String = "Seller/Order"

  override protected def applicationDescription: String = "Seller/Order"

    val getList = (apiOperation[Int]("getList")
        summary s"Get seller's order list"
        notes "Get different list from every filter"
        parameter bodyParam[String].description("""
            status: 0: all orders, 1: awaiting payment, 2: awaiting shipment, 3: paid and shipped, 
            duration: 0: 24 hours, 1: 3days, 2: 7days, 3: 14days, 4: 30days, 5: 60days, 6: 180days,
            searchKey: 0: NSLN, 1: Item Title, 2: OrderedId, 3: Buyer username, 
            searchWord: "", 
            sort: 0: date sold(ascending), 1: date sold(descending), 2: ship by date(ascending), 3: ship by date(descending) 
          """)
    )

    val getDetail = (apiOperation[Int]("getDetail")
      summary "Get details of Order"
    )
    val confirmShip = (apiOperation[Int]("confirmShip")
      summary "Confirm the shipment"
    )
    val contactBuyer = (apiOperation[Int]("contactBuyer")
      summary "Confirm the shipment"
    )
    val createLabel = (apiOperation[Int]("createLabel")
      summary "Create shipping label"
    )
    val downloadLabel = (apiOperation[Int]("downloadLabel")
      summary "Download label"
    )
    val reportBuyer = (apiOperation[Int]("reportBuyer")
      summary "Report buyer's action"
    )
    val refund = (apiOperation[Int]("refund")
      summary "Refund the money"
    )
    val cancelItem = (apiOperation[Int]("cancelItem")
      summary "Cancel item"
    )
}