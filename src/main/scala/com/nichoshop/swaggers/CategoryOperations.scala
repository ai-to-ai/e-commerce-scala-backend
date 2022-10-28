package com.nichoshop.swaggers

import com.nichoshop.legacy.models.{CategoriesRow, Manifests}

trait CategoryOperations extends ApiDescription[CategoriesRow] {

  override def name = "Category"

  implicit def manifestForT: Manifest[CategoriesRow] = Manifests.category


  val getSubcategories = (apiOperation[List[CategoriesRow]]("getSubcategories")
    summary "Find subcategories"
    notes "Find subcategories"
    parameter pathParam[Int]("parentId").description("category's parentId")
    )

  val sidebar = (apiOperation[List[CategoriesRow]]("sidebarCategories")
    summary "Returns categories with parent id 0 and their children"
    )

  val top = (apiOperation[List[CategoriesRow]]("top")
    summary "Returns categories with parent id 0 and their children"
  )

}
