package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._


/**
 * Created by Nursultan on 6/6/2022
 */

case class PostInfo(

            domesticService : Int = 1,
            domesticServiceCost : Int = 0,
            anotherService : Option[Int] = None,
            anotherServiceCost : Option[Int] = None,
            localCollect : Option[Boolean] = None,
            internationalService : Option[Int] = None,
            internationalServiceCost : Option[Int] = None,
            dispatchTime : Int = 0,
            itemCountry : Int = 0,
            itemCity : String,
            returnDays : Int,
            returns : Int = 0,
            returnAccept : Boolean = true

)

case class ItemEntity(

            override val id: Option[Int] = None,
            sellerId: Int,
            catId : Int, 
            title: String, 
            condId: Int, 
            condDesc: Option[String] = None, 
            images: List[String] = List(),
            image: String,
            // itemSpecific: List[Map[String,Any]],
            itemDesc: Option[String],
            listingFormat: Int = 0,
            nowPrice: Int = 0,
            currency: Int,
            quantity: Int = 0,
            duration: Int = 0,
            startPrice : Int = 0,
            reservePrice : Int = 0,
            state: Int = 0,
            status: Int = 0,
            isMultivariation: Boolean = false,
            created: Long = System.currentTimeMillis(),
            nsln: String = "",
            postInfo: PostInfo

) extends IdAsPrimaryKey

class Items(tag:Tag) extends TableWithIdAsPrimaryKey[ItemEntity](tag, "items") {
  implicit val commaSeparatedString = separatedString()

  def sellerId = column[Int]("seller_id")
  def catId = column[Int]("cat_id")
  def title = column[String]("title")
  def condId = column[Int]("cond_id")
  def condDesc = column[Option[String]]("cond_desc")
  def images = column[List[String]]("images")
  def image = column[String]("image")
  def itemDesc = column[Option[String]]("item_desc")
  def listingFormat = column[Int]("listing_format")
  def nowPrice = column[Int]("now_price")
  def currency = column[Int]("currency")
  def quantity = column[Int]("quantity")
  def duration = column[Int]("duration")
  def state = column[Int]("state")
  def status = column[Int]("status")
  def isMultivariation = column[Boolean]("is_multivariation")
  def startPrice = column[Int]("start_price")
  def reservePrice = column[Int]("reserve_price")
  def created = column[Long]("created")
  def nsln = column[String]("nsln")
  
  def domesticService = column[Int]("domestic_service")
  def domesticServiceCost = column[Int]("domestic_service_cost")
  def anotherService = column[Option[Int]]("another_service")
  def anotherServiceCost = column[Option[Int]]("another_service_cost")
  def localCollect = column[Option[Boolean]]("local_collect")
  def internationalService = column[Option[Int]]("international_service")
  def internationalServiceCost = column[Option[Int]]("international_service_cost")
  def dispatchTime = column[Int]("dispatch_time")
  def itemCountry = column[Int]("item_country")
  def itemCity = column[String]("item_city")
  def returnDays = column[Int]("return_days")
  def returns = column[Int]("returns")
  def returnAccept = column[Boolean]("return_accept")

  def post = ( domesticService, domesticServiceCost, anotherService, anotherServiceCost, localCollect, internationalService, internationalServiceCost, dispatchTime, itemCountry, itemCity, returnDays, returns, returnAccept) <>((PostInfo.apply _).tupled, PostInfo.unapply)

  def * = (id.?, sellerId, catId, title, condId, condDesc, images, image, itemDesc, listingFormat, nowPrice, currency, quantity, duration,  startPrice, reservePrice, state, status, isMultivariation, created, nsln, post ) <> (ItemEntity.tupled, ItemEntity.unapply)

}

object Items extends QueryForTableWithIdAsPrimaryKey[ItemEntity, Items](TableQuery[Items]){
}

