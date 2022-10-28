package com.nichoshop.controllers

import com.nichoshop.utils
import org.scalatra._
import javax.servlet.http.HttpServletRequest
import com.nichoshop.auth.AuthenticationSupport

import org.json4s.JsonAST.{JObject, JString}
import org.json4s.jackson.JsonMethods._
import scala.util.control.NonFatal
import org.scalatra.CorsSupport

trait BaseController extends ScalatraServlet with utils.Json with utils.Logger with CorsSupport{

    def name : String

    options("/*"){
      response.setHeader(
        "Access-Control-Expose-Headers", "Set-Cookie");
    }

    def getIpFromRequest(implicit request: HttpServletRequest): String = {
        val ip = request.getHeader("X-FORWARDED-FOR")
        if (ip == null) {
        request.getRemoteAddr
        } else ip
    }

    //  in case not matching url exists. @dev

    notFound {
      log.error(s"${request}")
      com.nichoshop.page.html.notFound.render(request.getRequestURI)
    }

    // in case something went wrong.

    error {
      case NonFatal(e) =>
        log.error("exception happens: ", e)
        halt(400, compact(JObject(List("error" -> JString(e.getMessage),"author"->JString("Nursultan Saudirbayev")))))
    }

}
