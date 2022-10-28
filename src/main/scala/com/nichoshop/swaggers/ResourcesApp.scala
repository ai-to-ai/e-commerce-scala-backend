package com.nichoshop.swaggers

import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{ApiInfo, JacksonSwaggerBase, Swagger}

class ResourcesApp(implicit val swagger: Swagger) extends ScalatraServlet with JacksonSwaggerBase


object NichoshopApiInfo extends ApiInfo(
  "The nichoshop API",
  "Docs for the nichoshop API",
  "http://nichoshop.com",
  "bestlisum@gmail.com",
  "MIT",
  "http://opensource.org/licenses/MIT")

class NichoSwagger extends Swagger(Swagger.SpecVersion, "1.0.0", NichoshopApiInfo)