package com.nichoshop.utils

import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{NotFound, Ok}
import com.nichoshop.model.dto.AccountType
import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString

import javax.servlet.http.HttpServletRequest

trait Json extends JacksonJsonSupport {
  protected implicit val jsonFormats: Formats = DefaultFormats

  def name: String

  before() {
    contentType = formats("json")
  }

  implicit class List2Paging[T](l: List[T]) {
    def paged(count: Int, page: Int) = {
      val pages = l.grouped(count).toList
      if (page - 1 >= pages.size) Nil
      else pages(page - 1)
    }

    def paged(implicit request: HttpServletRequest): List[T] = paged(params("count").toInt, params("page").toInt)
  }

  implicit class Boolean2Digit(b: Boolean) {
    def toDigit = if (b) 1 else 0
  }

  implicit class lowerCase(s : String) {
    def toLowerCase = s.toLowerCase
  }

}
