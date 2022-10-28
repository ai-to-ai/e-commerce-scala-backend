package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 6/7/2022
 */

 case class CartEntity(

            override val id: Option[Int] = None,
            userId: Int,
            itemId: Int,
            variantId: Int,
            qty: Int,
            flag: Boolean = false

 ) extends IdAsPrimaryKey

class Carts(tag:Tag) extends TableWithIdAsPrimaryKey[CartEntity](tag, "carts") {

    def userId = column[Int]("user_id")
    def itemId = column[Int]("item_id")
    def variantId = column[Int]("variant_id")
    def qty = column[Int]("qty")
    def flag = column[Boolean]("flag")

    def * = (id.?, userId, itemId, variantId, qty, flag) <> (CartEntity.tupled, CartEntity.unapply)

}

object Carts extends QueryForTableWithIdAsPrimaryKey[CartEntity, Carts](TableQuery[Carts]){
    
}