package com.nichoshop.controllers

import com.nichoshop.services.Services
import com.nichoshop.swaggers.ItemOperations
import com.nichoshop.utils.{Marshallers, Pagination, Constants}
import com.nichoshop.models.{ ItemEntity, Items, PostInfo}

import org.scalatra.swagger.Swagger
import org.json4s.JsonAST.JArray
import org.json4s.JsonAST.{JBool, JObject, JString}

import org.scalatra.servlet.{FileUploadSupport, MultipartConfig, SizeConstraintExceededException}

class ItemController(implicit val swagger: Swagger) extends AuthController with ItemOperations with Pagination with FileUploadSupport{
    
    configureMultipartHandling(MultipartConfig(maxFileSize = Some(Constants.itemPhotoSize)))
    
    val itemService = Services.itemService


     get("/:catId") {
        val catId = params.getOrElse("catId", "0").toInt
        // Marshallers.toJson(itemService.getItems(catId))
    }

    get("/search", operation(search)) {
        val str = params.get("str")
        val catId = params.get("catId")

        (str, catId) match {
        case (Some(s), Some(id)) => itemService.searchWithCategoryId(id.toInt, s)
        case (Some(s), None) => itemService.search(s)
        case (None, Some(id)) => itemService.filterByCategory(id.toInt)
        case _ => itemService.findAll
        }
    }

    get("/bestProducts", operation(bestProducts)) {
        // itemService.bestProducts()
    }

    get("/active", operation(active)) {
        val filter = params.getOrElse("filter","0").toInt
        itemService.active(uid,filter)
    }

    get("/sold", operation(sold)) {
        val filter = params.getOrElse("filter","0").toInt
        itemService.sold(uid, filter)
    }

    get("/unsold", operation(unsold)) {
        itemService.unsold(uid)
    }

    get("/archive", operation(archive)) {
        val filter = params.getOrElse("filter","0").toInt
        itemService.archive(uid,filter)
    }

    get("/watchlist", operation(watchlist)) {
        itemService.watchlist(uid)
    }

    put("/dispatched", operation(markAsDispatched)) {
        val sellId = params.getOrElse("sellId","0").toInt
        // sellService.markAsDispatched(uid, sellId)
    }

    put("/tracking", operation(addTracking)) {
        val sellId = params.getOrElse("sellId","0").toInt
        val number = params.getOrElse("number","")
        val courier = params.getOrElse("courier","")
        // sellService.addTracking(uid, sellId, number, courier)
    }

    delete("/", operation(deleteMarked)) {
        itemService.deleteMarked(uid, parsedBody.extract[List[Int]])
    }

    get("/recent", operation(recent)){
        val userId = params.getOrElse("userId","0").toInt
        val itemsList = itemService.getRecentItems(userId)
        // val itemsList = itemService.getRecentItems(uid)

        compact(JObject(List(
            Some("list" -> JArray( itemsList.map(item => JObject(
                List(
                    Some("image" -> JString(item.image)),
                    Some("nowPrice" -> JString(item.nowPrice.toString)),
                    Some("currency" -> JString(item.currency.toString))
                ).flatten
            ))))
        ).flatten))

    }

    post("/create", operation(create)){
        val createItem = parsedBody.extract[CreateItem]
        val userId = params.getOrElse("userId","0").toInt
        val itemEntity =  new ItemEntity(
            id = None,
            sellerId = userId,
            // sellerId = uid,
            catId = createItem.catId, 
            title = createItem.title, 
            condId = createItem.condId, 
            condDesc = createItem.condDesc, 
            images = createItem.images,
            image = createItem.image,
            // itemSpecific: List[Map[String,Any]],
            itemDesc = createItem.itemDesc,
            listingFormat = createItem.listingFormat,
            nowPrice = createItem.nowPrice,
            currency = createItem.currency,
            quantity = createItem.quantity,
            duration = createItem.duration,
            startPrice  = createItem.startPrice,
            reservePrice = createItem.reservePrice,
            state = createItem.state,
            status = createItem.status,
            isMultivariation = createItem.isMultivariation,
            postInfo = new PostInfo(
                domesticService  = createItem.postInfo.domesticService,
                domesticServiceCost  = createItem.postInfo.domesticServiceCost,
                anotherService  = createItem.postInfo.anotherService,
                anotherServiceCost  = createItem.postInfo.anotherServiceCost,
                localCollect = createItem.postInfo.localCollect,
                internationalService = createItem.postInfo.internationalService,
                internationalServiceCost  = createItem.postInfo.internationalServiceCost,
                dispatchTime  = createItem.postInfo.dispatchTime,
                itemCountry  = createItem.postInfo.itemCountry,
                itemCity  = createItem.postInfo.itemCity,
                returnDays  = createItem.postInfo.returnDays,
                returns  = createItem.postInfo.returns,
                returnAccept = createItem.postInfo.returnAccept
            )
    
        )

        itemService.create(itemEntity)
        Marshallers.ok()
    }

    // post("/file") {
    //     processFile(fileParams("file"))
    //     // val file = fileParams.get("file") 
    // }

}

case class CreateItem(
            id: Option[Int] = None,
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
            postInfo: PostInfo
)