package com.nichoshop.swaggers

import com.nichoshop.models.{Items, ItemEntity}
import com.nichoshop.controllers.CreateItem
import org.scalatra.swagger.SwaggerSupport

trait ItemOperations extends SwaggerSupport {

  def name: String = "Item"

  override protected def applicationDescription: String = "Item"

  val search = (apiOperation[List[ItemEntity]]("search")
    summary s"Search for Products containing str in title"

    notes "To search in all categories don't use catId param"
    parameters(
      queryParam[String]("str").description("string to search in item title").optional,
      queryParam[Int]("catId").description("category id to filter with").optional)
    )

  val bestProducts = (apiOperation[List[ItemEntity]]("bestProducts")
    summary s"Get 12 random items from Daily Deals category"

    )

  val active = (apiOperation[List[ItemEntity]]("active")
    summary s"Returns user's active sells"

    parameters(
      queryParam[Int]("filter").description("0 - all, 1 - fixed price, 2 - with open offers, 3 - auctions, 4 - classified ads"),
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("count").description("sells per page"))
    )

  val sold = (apiOperation[List[ItemEntity]]("sold")
    summary s"Returns user's sold items"

    parameters(
      queryParam[Int]("filter").description("0 - all, 1 - awaiting payment, 2 - awaiting dispatch, 3 - dispatched, 4 - awaiting feedback"),
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("count").description("sells per page"))
    )

  val unsold = (apiOperation[List[ItemEntity]]("unsold")
    summary s"Returns user's unsold items (expired)"

    parameters(
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("count").description("sells per page"))
    )

  val archive = (apiOperation[List[ItemEntity]]("archive")
    summary s"Returns user's selling archive"


    parameters(
      queryParam[Int]("filter").description("0 - all, 1 - Price: highest first, 2 - Price: lowest first, 3 - Added: newest first, 4 - Added: oldest first"),
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("count").description("sells per page"))
    )

  val deleteMarked = (apiOperation[Unit]("deleteMarked")
    summary s"Removes marked items"

    parameter bodyParam[List[Int]].description("Array of marked item ids")
    )

  val addTracking = (apiOperation[Unit]("addTracking")
    summary s"Add tracking number and courier t sell"

    parameters(
      queryParam[String]("number").description("Tracking number"),
      queryParam[String]("courier").description("Courier name"),
      queryParam[Int]("sellId").description("Sell id")
    )
  )

  val markAsDispatched = (apiOperation[Unit]("markAsDispatched")
    summary s"Mark sell as dispatched"

    parameter queryParam[Int]("sellId").description("Sell id")
    )

  val watchlist = (apiOperation[List[ItemEntity]]("watchlist")
    summary s"Returns user's watchlist"

    parameters(
      queryParam[Int]("page").description("page number"),
      queryParam[Int]("count").description("sells per page"))
    )

    val recent = (apiOperation[List[ItemEntity]]("recent")
      summary s"Returns user's recent viewed list"
    )
    val create = (apiOperation[Unit]("create")
      summary s"Create a item(listing)"

      parameter bodyParam[CreateItem].description("item paramter for creation.")

    )
}