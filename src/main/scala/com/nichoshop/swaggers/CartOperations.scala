package com.nichoshop.swaggers

import com.nichoshop.models.CartEntity
import com.nichoshop.model.dto.CartDto
import org.scalatra.swagger.SwaggerSupport
import com.nichoshop.controllers.CartBody

trait CartOperations extends SwaggerSupport {

  def name: String = "Cart"

  override protected def applicationDescription: String = "Cart"

    val addItemToCart = (apiOperation[Unit]("addItemToCart")
        summary "Add a cart to user's shopping carts"
        notes   "parameters : itemId, itemVariantId, qty"
        parameter bodyParam[CartBody].description("content-type:json")
    )

    val getCartItems = (apiOperation[List[CartDto]]("getCartItems")
        summary "Returns user's shopping carts"
    )

    val saveForLater = (apiOperation[Unit]("saveForLater")
        summary "Save a item for later."

        parameter queryParam[Int]("id").description("cart's id, not item's id")
    )

    val moveToCart = (apiOperation[Unit]("moveToCart")
        summary "Save a item to cart."
        
        parameter queryParam[Int]("id").description("cart's id, not item's id")
    )

    val getSummary = (apiOperation[Unit]("getSummary")
        summary "Get cart's count for a certain user."
        
    )
}
