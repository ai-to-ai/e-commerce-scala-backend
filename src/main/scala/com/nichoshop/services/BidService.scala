package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers.DB
import com.nichoshop.models.{BidEntity, Bids, Items, ItemEntity}
import com.nichoshop.utils.Pagination

import org.slf4j
import org.slf4j.LoggerFactory

import java.util.concurrent.TimeUnit

/** 
 *  Created by Nursultan on 6/7/2022
 */

class BidService {

    
    val bids = Bids.query
    val items = Items.query
    
    /**
     *  getList() should adapt Bidding feature.
     *  @return BidDto 
     */

    def getList(userId: Int)(implicit paging: (Int, Int)) : List[BidEntity] = DB.read { implicit session =>
        val (page, limit) = paging
        bids.sortBy(_.created.desc)
            .drop(page)
            .take(limit).list
    }

    def addBid(x: BidEntity) :Unit = DB.write { implicit session =>
        bids.insert(x)
    }

    def isCancellable(id: Int) : Boolean = DB.read { implicit session =>
        var result = true
        var itemList = (for{
            bid <- bids
            item <- items
            if bid.id === id
            if (item.id === bid.itemId && item.listingFormat === 1)
        } yield (item)).list

        if (itemList.size > 0) itemList.foreach{ item =>{
                                if((item.created + TimeUnit.DAYS.toMillis(item.duration) < (System.currentTimeMillis + TimeUnit.HOURS.toMillis(1))) ) result = false
                                else result = false
                            }
        }
        result
    }

    def isAuction(id : Int) : Boolean = DB.read { implicit session =>
        val item = items.filter(_.id === id).take(1).list.headOption
        item match {
            case Some(i) => if(i.listingFormat == 1) true else false
            case None => false
        }

    }

    def cancelBid(id: Int) :Unit = DB.write { implicit session =>
        bids.filter(_.id === id).delete
    }

    def getBidCount(userId: Int) :Map[String, Int] = DB.read { implicit session =>
        val all = bids.filter(_.userId === userId).list.size
        val win = bids.filter(_.userId === userId).list.filter(bid => {
            val maxAmount = bids.groupBy(b => b.itemId)
                                            .map { case (itemId, b) =>  (itemId,b.map(_.bidAmount).max) }
                                            .filter{ case (itemId, max) => itemId === bid.itemId }
                                            .map(_._2)
                                            .list.head
            if(maxAmount.isDefined)
               if( (maxAmount.get) <= bid.bid.amount ) true
               else false
            else false
        }).size
        val lose = all - win

        Map("all"->all,"win"->win,"lose"->lose)
    }

}