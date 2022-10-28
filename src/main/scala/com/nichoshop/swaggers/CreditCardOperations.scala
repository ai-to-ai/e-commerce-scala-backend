package com.nichoshop.swaggers

import com.nichoshop.models.{CreditCardEntity}
import org.scalatra.swagger.SwaggerSupport

trait CreditCardOperations extends SwaggerSupport {

  def name: String = "CreditCard"

  override protected def applicationDescription: String = "CreditCard"

//   val getSubcategories = (apiOperation[List[Items]]("getSubcategories")
//         summary "Find subcategories"
//         notes "Find subcategories"
//         parameter pathParam[Int]("parentId").description("category's parentId")
//     )

}
