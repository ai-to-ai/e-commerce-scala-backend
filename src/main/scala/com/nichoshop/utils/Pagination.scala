package com.nichoshop.utils

import org.scalatra.ScalatraServlet

trait Pagination {
  self: ScalatraServlet =>

  implicit lazy val pagination = (params.getOrElse("page","1").toInt, params.getOrElse("limit", "10").toInt)
}