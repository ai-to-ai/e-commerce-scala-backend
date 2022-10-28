package com.nichoshop.swaggers

import com.nichoshop.models.OrderEntity
import com.nichoshop.model.dto.OrderDetailDto
import com.nichoshop.controllers.{OrderFilter,OrderBody}
import org.scalatra.swagger.SwaggerSupport

trait OrderOperations extends SwaggerSupport {

  def name: String = "Order"

  override protected def applicationDescription: String = "Order"

    val getList = (apiOperation[List[OrderDetailDto]]("getList")
        summary "Find all orders"
        parameters (
            queryParam[Int]("duration").description("The duration filter."),
            queryParam[Int]("status").description("The FilterBy, contains All,delivered, shipped, not shipped, awaiting feecback."),
            queryParam[Int]("page").description("page to show."),
            queryParam[Int]("limit").description("Orders' count in a page.")
        )
    )
    
    val getDetails = (apiOperation[OrderDetailDto]("getDetails")
        summary "Get details of a certain order"
        parameter queryParam[Int]("id").description("The order's id for detail.")
    )

    val createOrder = (apiOperation[Unit]("createOrder")
        summary "Create a new order"
        parameter bodyParam[OrderBody].description("Contains order info.")
    )

    val deleteOrder = (apiOperation[Unit]("deleteOrder")
        summary "Delete a order"
        parameter queryParam[Int]("id").description("The order's id to delete.")
    )

}
