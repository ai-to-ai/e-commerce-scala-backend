package com.nichoshop.swaggers


import com.nichoshop.legacy.models.AddressesRow
import org.scalatra.swagger.SwaggerSupport

trait AddressOperations extends SwaggerSupport {

  def name : String = "Address"

  override protected def applicationDescription: String = "Address"


  val getAdresses = (apiOperation[AddressesRow]("getAdresses")
    summary s"Return Addresses"
    parameters(formParam[String]("userid").description("userid"))
    )

}
