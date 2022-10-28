package com.nichoshop.controllers

import com.nichoshop.swaggers.CartOperations
import org.scalatra.swagger.Swagger
import com.nichoshop.utils
import com.nichoshop.models.CartEntity

import com.nichoshop.services.Services

class CartController(implicit val swagger: Swagger) extends AuthController with CartOperations with utils.Pagination {

    val cartService = Services.cartService

    get("/", operation(getCartItems)){
        cartService.getCartItems(uid)
    }
    post("/", operation(addItemToCart)){
        val cartBody = parsedBody.extract[CartBody]
        val cartEntity = new CartEntity(None, uid, cartBody.itemId, cartBody.itemVariantId,cartBody.qty)
        
        cartService.addItemToCart(cartEntity)
    }

    put("/save-for-later", operation(saveForLater)){
        val id = params.getOrElse("id","0").toInt

        cartService.saveForLater(id)
    }
    put("/move-to-cart", operation(moveToCart)) {
        val id = params.getOrElse("id","0").toInt

        cartService.moveToCart(id)
    }

    get("/summary", operation(getSummary)){
        cartService.getSummary(uid)
    }
}

case class CartBody(itemId: Int, itemVariantId: Int, qty: Int)