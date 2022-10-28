package com.nichoshop.controllers

import com.nichoshop.utils.Marshallers

import com.nichoshop.services.Services
import com.nichoshop.swaggers.CategoryOperations
import org.json4s.JsonAST.JArray
import org.json4s.JsonAST.{JBool, JObject, JString}

import org.scalatra.swagger.Swagger

import scala.collection.JavaConversions._
/**
 * Created by Nursultan on 6.14.2022.
 */
class CategoryController(implicit val swagger: Swagger)  extends BaseController with CategoryOperations  {

  val categoryService = Services.categoryService
  val itemService = Services.itemService

  get("/top", operation(top)) {
    Marshallers.toJson(categoryService.sidebarCategories2)
    
  }

  get("/all") {
    Marshallers.toJson(categoryService.categoriesTree)
  }

  get("/children/:parentId") {
    Marshallers.toJson(categoryService.categoryList(params("parentId").toInt))
  }

  get("/search") {
    val keyword = params.getOrElse("keyword", "")

    itemService.searchforCategoryList(keyword)
    // Marshallers.toJson(categoryService.getCategoryfromList(cList))
  }

  get("/list") {
    categoryService.getList
  }

}

