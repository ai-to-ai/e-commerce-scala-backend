package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import scala.slick.lifted.Tag

/**
 * Created by Nursultan on 5/19/2022.
 */
case class BusinessEntity(

            override val id: Option[Int] = None,
            userId: Int,
            name: String,
            addressId: Option[Int],
            vatCountry: String,
            vatNumber: String
          
) extends IdAsPrimaryKey

class Businesses(tag:Tag) extends TableWithIdAsPrimaryKey[BusinessEntity](tag, "businesses") {
  def userId = column[Int]("user_id")

  def name = column[String]("name")

  def addressId = column[Option[Int]]("address_id")

  def vatCountry = column[String]("vat_country")

  def vatNumber = column[String]("vat_number")

  def * = ( id.?, userId, name, addressId, vatCountry, vatNumber) <> ( BusinessEntity.tupled, BusinessEntity.unapply )
}

object Businesses extends QueryForTableWithIdAsPrimaryKey[BusinessEntity, Businesses](TableQuery[Businesses]) {
}
