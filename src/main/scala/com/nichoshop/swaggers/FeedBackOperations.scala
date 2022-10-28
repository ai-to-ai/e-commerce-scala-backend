package com.nichoshop.swaggers

import com.nichoshop.models.FeedBackEntity
import org.scalatra.swagger.SwaggerSupport

trait FeedBackOperations extends SwaggerSupport {

  def name: String = "FeedBack"

  override protected def applicationDescription: String = "FeedBack"

//   val getSubcategories = (apiOperation[List[Items]]("getSubcategories")
//         summary "Find subcategories"
//         notes "Find subcategories"
//         parameter pathParam[Int]("parentId").description("category's parentId")
//     )

}
