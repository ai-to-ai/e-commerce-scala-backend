package com.nichoshop.controllers.admin

import com.nichoshop.utils.Marshallers
import com.nichoshop.model.dto._
import com.nichoshop.utils.Converters._
import com.nichoshop.utils.Conversions._
import com.nichoshop.utils.CaseClasses._
import com.nichoshop.services.{PermissionsService, Services}
import com.nichoshop.controllers.{AuthController,BaseController}

import org.json4s.JsonAST.JArray

import scala.collection.JavaConversions._

/**
 * Modified by Nursultan on 6/30/2022
 */

class CategoryController extends AuthController {

  def name = "admin/category"

  val categoryService = Services.categoryService
  val auth = Services.authService

  get("/conditions"){
    categoryService.getConditionsList
  }

  get("/specifics"){
    categoryService.getSpecificsList
  }

  get("/top") {
    Marshallers.toJson(categoryService.sidebarCategories2)
  }

  get("/all") {
    Marshallers.toJson(categoryService.categoriesTree)
  }

  get("/children/:parentId") {
    Marshallers.toJson(categoryService.categoryList(params("parentId").toInt))
  }

  post ("/parent/:parentId") {
    val c = parsedBody.extract[CreateCategory]

    auth.withUser { user =>() =>

      categoryService.createCategory(c.name, params("parentId").toInt)
      Marshallers.ok()
    }
  }

  put ( "/:categoryId" ) {
    val category = parsedBody.extract[UpdateCategory]

    auth.withUser { user =>() =>

      val result = categoryService.updateCategory(params("categoryId").toInt, category.conditions.filterNot(_.isEmpty), Some(toSpecificArrayDto(category.specifics)))
      if(result > 0) Marshallers.ok() else Marshallers.bad("Cannot update category.")
    }
  }

  put ("/parent/:parentId/:childId") {
    auth.withUser { user =>() =>

      categoryService.moveCategory(params("parentId").toInt, params("childId").toInt)
      Marshallers.ok()
    }
  }

  put("/rename/:categoryId/:name") {
    auth.withUser { user =>() =>
      
      categoryService.renameCategory(params("categoryId").toInt, params("name"))
      Marshallers.ok()
    }
    
  }
  
  delete("/:categoryId") {
    auth.withUser { user =>() =>

        categoryService.removeCategory(params("categoryId").toInt)
        Marshallers.ok()
    }
  }

  post("/:categoryId/specifics") {
    val s = parsedBody.extract[CreateSpecific]

    auth.withUser { user =>() =>

      categoryService.createSpecific(s.name)  
      Marshallers.ok()
    }
  }

}

