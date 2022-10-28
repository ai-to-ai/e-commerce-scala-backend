package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers.DB
import com.nichoshop.models.{OrderEntity, Orders, CancelOrderEntity, CancelOrders, ItemEntity, Items, VariantEntity, Variants}
import com.nichoshop.model.dto.OrderItemDto
import com.nichoshop.utils.{Pagination, SecureUtils}
import com.nichoshop.controllers.OrderFilter

import org.slf4j
import org.slf4j.LoggerFactory

import java.util.Calendar
import java.util.concurrent.TimeUnit

class OrderService {

    val orders = Orders.query
    val cancelOrders = CancelOrders.query
    val items = Items.query
    val variants = Variants.query

    def ordersByFilter(userId: Int, oFilter: OrderFilter) = {
            val orderList = orders.filter(_.buyerId=== userId)

             val orderListwithDuration =  oFilter.duration match {
                case 0 => orderList.filter(_.created > (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(90)))
                case 1 => orderList.filter(_.created > SecureUtils.year2time(Calendar.getInstance.get(Calendar.YEAR)) )
                case 2 => orderList.filter(_.created > SecureUtils.year2time(Calendar.getInstance.get(Calendar.YEAR) - 1))
                case 3 => orderList.filter(_.created > SecureUtils.year2time(Calendar.getInstance.get(Calendar.YEAR) - 2))
             }                      
             
             if(oFilter.status >0) orderListwithDuration.filter( o => o.status === oFilter.status)
             else orderListwithDuration
        }


    def getList(userId: Int, oFilter: OrderFilter)( implicit paging: (Int, Int)) = {
        val (page, limit) = paging

        val orderList = DB.read { implicit session =>
                ordersByFilter(userId, oFilter).drop((page-1)*limit)
                            .take(limit).list
        }
        
        // orderList.map(OrderItemDto)
    }

    def getDetails(id: Int) = DB.write { implicit session =>
        val order = orders.filter(_.id === id).take(1).list.headOption


    }

    def createOrder(x: OrderEntity) = DB.write { implicit session=>
        orders.insert(x)
    }

    def deleteOrder(id: Int) = DB.write{ implicit session =>
        orders.filter(_.id === id).delete
    }

    def getNumofDay(): Unit = DB.read { implicit session =>
        orders.filter(_.created > SecureUtils.day2time())
                .sortBy(_.created.desc)
                .list.size
    }

    def getOrderBySeller(userId: Int, sellerId: Int) = {
        // val ordersList = DB.read { implicit session =>
        //     (for {
        //         order <- orders
        //         item <- items

        //         if (order.sellerId == sellerId && order.userId == userId)
        //         if (order.itemId == item.id)
        //     } yield(order, item)).list
        // }
        // ordersList.map(o => )
    }
    
    def canceledOrders(userId: Int) = DB.read { implicit session =>
        orders.filter(_.buyerId === userId).filter(_.cancel === true ).list
    }

    def CancelOrderRequest(x: CancelOrderEntity) = DB.write { implicit session =>
        cancelOrders.insert(x)
    }

    def acceptCancelOrder(id: Int) = DB.write { implicit session =>
        orders.filter(o => o.id in cancelOrders.filter(_.id === id).map(_.orderId))
                .map(o=>(o.cancel,o.cancelDate))
                .update(true, Option(System.currentTimeMillis()))

        cancelOrders.filter(_.id === id).map(_.accepted).update(true)    
    }

}