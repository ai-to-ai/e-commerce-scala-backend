package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.model.dto._
import com.nichoshop.models.helpers._
import com.nichoshop.utils.Converters
import com.nichoshop.models.{Carts, CartEntity, Items,ItemEntity, Variants}
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

/**
 * Created by Nursultan on 6/7/2022
 */
class CartService {
  private val log = LoggerFactory.getLogger(getClass)

    val items = Items.query
    val variants = Variants.query
    val carts = Carts.query

    def addItemToCart(x: CartEntity) = DB.write { implicit session =>
        carts.insert(x)
    }

    def getCartItems(userId: Int) : List[ItemEntity] = DB.read  { implicit session =>
        (for {
            cart <- carts
            item <- items
            if((cart.itemId === item.id) && (cart.userId === userId))
        } yield(item)).list

        // val itemDtoList: List[ItemDto] = DB.read { implicit session =>
        //     (for {
        //         cart <- carts
        //         item <- items
        //         variant <- variants
        //         if (item.id === cart.itemId) && (item.id === variant.itemId) && (variant.id === cart.itemVariantId)
        //         if (cart.userId === userId)
        //         } yield (item, variant, cart)).list.map {case (item, variant, cart) =>
        //         (item, variant.copy(amount = cart.qty) : ItemDto)
        //         }
        // }
        // val cartItemDtoList : List
        // itemDtoList.foreach{
        //     itemDto => 
        // }
        // val cartItemDto = CartItemDto.newBuilder().
        //                         setProduct(item).
        //                         setQty(qty).build
        // val cart = CartDto.newBuilder().
        //                     setItems(seqAsJavaList(Seq(item))).
        //                     build()
        //     cart
    }

    def delteFromCart(id: Int) : Unit = DB.write { implicit session =>
        carts.filter(_.id === id).delete    
    }

    def saveForLater(id: Int) : Unit = DB.write { implicit session =>
        carts.filter(_.id === id).map(_.flag).update(false)
    }

    def moveToCart(id: Int) : Unit = DB.write { implicit session =>
        carts.filter(_.id === id).map(_.flag).update(true)
    }

    def getSummary(userId: Int) : Unit = DB.read { implicit session =>
        carts.filter(_.flag === true)
            .filter(_.userId === userId)
            .list.size    
    }

}
