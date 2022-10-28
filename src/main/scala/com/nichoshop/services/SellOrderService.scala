package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers.DB
import com.nichoshop.models.{OrderEntity, Orders, CancelOrderEntity, CancelOrders, ItemEntity, Items, VariantEntity, Variants, Users, UserEntity}
import com.nichoshop.model.dto._
import com.nichoshop.utils.Converters._
import com.nichoshop.utils.{Pagination, SecureUtils}
import com.nichoshop.controllers.SellerOrderFilter

import org.slf4j
import org.slf4j.LoggerFactory

import java.util.Calendar
import java.util.concurrent.TimeUnit

class SellOrderService {
    val orders = Orders.query
    val items = Items.query
    val users = Users.query
    val variants = Variants.query

    private def ordersByFilter(uid: Int,x: SellerOrderFilter) = {

        // Filter by Seller.
        val ordersBySeller = orders.filter(_.sellerId === uid)

        // Filter by Status.
        val ordersByStatus = if(x.status >0) ordersBySeller.filter(_.status === x.status) else ordersBySeller

        // Filter by Duration.
        val ordersByDuration = x.duration match {
            case 0 =>   ordersByStatus.filter(_.created > (System.currentTimeMillis() - TimeUnit.HOURS.toMillis(24)))
            case 1 =>   ordersByStatus.filter(_.created > (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3)))
            case 2 =>   ordersByStatus.filter(_.created > (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)))
            case 3 =>   ordersByStatus.filter(_.created > (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(14)))
            case 4 =>   ordersByStatus.filter(_.created > (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)))
            case 5 =>   ordersByStatus.filter(_.created > (System.currentTimeMillis() - TimeUnit.DAYS.toMillis(60)))

        }
        
        // Filter by SearchKeyword
        val ordersBySearch = x.searchKey match {
            case 0 =>   ordersByDuration.filter(o => o.itemId in items.filter(_.nsln like s"%s${x.searchWord}%s").map(_.id))
            case 1 =>   ordersByDuration.filter(o => o.itemId in items.filter(_.title like s"%s${x.searchWord}%s").map(_.id))
            case 2 =>   ordersByDuration.filter(_.orderId === x.searchWord)
            case 3 =>   ordersByDuration.filter(o => o.buyerId in users.filter(_.userid like s"%s${x.searchWord}%s").map(_.id))
        }

        // Filter by Sort.
        val ordersBySort = x.sort match {
            case 0 =>   ordersBySearch.sortBy(_.soldDate.asc)
            case 1 =>   ordersBySearch.sortBy(_.soldDate.desc) 
            case 2 =>   ordersBySearch.sortBy(_.shipByDate.asc)
            case 3 =>   ordersBySearch.sortBy(_.shipByDate.desc)
        }

        // Return filtered result.
        ordersBySort
    }

    def getList(userId: Int, oFilter: SellerOrderFilter)( implicit paging: (Int, Int)) = {
        val (page, limit) = paging

        DB.read { implicit session =>
            ordersByFilter(userId, oFilter)
                .drop((page-1)*limit)
                .take(limit).list.map{
                    o => {
                           val orderItemsList =  o.items.map {
                               oi => 
                                    (for {
                                        item <- items
                                        if item.id === oi.toInt
                                    } yield item).firstOption.map {item =>
                                        val variantsList = (for {
                                        variant <- variants
                                        if variant.itemId === item.id
                                        } yield variant).list
        
                                    (item, variantsList): ItemDto
                                }.get
                            }

                        (o : OrderEntity, orderItemsList) : SellerOrderDto
                    }
                }
        }
    }

    def getOrder(id: Int) = DB.write { implicit session =>
        orders.filter(_.id === id).run.headOption.map(
            o => o.items.foreach{
                i => val orderItemsList = (for {
                        item <- items
                        if item.id === id
                    } yield item).run.headOption.map {item =>
                        val variantsList = (for {
                        variant <- variants
                        if variant.itemId === item.id
                        } yield variant).list

                        (item, variantsList): ItemDto
                    }.get
                    (o : OrderEntity, orderItemsList) : SellerOrderDto
            }
        )

    }

    def getDetail(id: Int) = DB.read { implicit session =>
        // val orderList = orders.filter(_.id === id).take(1).list.map {
        //     o => o : OrderDetailDto
        // }
        orders.filter(_.id === id).take(1).list
    }

    def confirmShipment(id: Int) = DB.read { implicit session =>
        orders.filter(_.id === id).map(_.status).update(2)
    }
}