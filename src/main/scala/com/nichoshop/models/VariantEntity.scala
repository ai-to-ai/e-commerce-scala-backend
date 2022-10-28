package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import scala.slick.lifted.Tag

/**
 * Created by Nursultan on 6/8/2022.
 */
case class VariantEntity (

            override val id: Option[Int],
            itemId: Int,
            title: Option[String],
            description: Option[String],
            condition: String,
            price: RichMoney,
            amount: Int,
            created: Long = System.currentTimeMillis()

  ) extends IdAsPrimaryKey


class Variants(tag:Tag) extends TableWithIdAsPrimaryKey[VariantEntity](tag, "variants") {
  def itemId = column[Int]("item_id")
  def title = column[Option[String]]("title")
  def description = column[Option[String]]("description")
  def condition = column[String]("condition")
  def amount = column[Int]("amount")
  def priceAmount = column[Int]("price")
  def priceCurrencyId = column[Int]("currency_id")
  def created = column[Long]("created")

  def price = (priceAmount, priceCurrencyId) <> (RichMoney.tupled, RichMoney.unapply)

  def * = (id.?, itemId, title, description, condition, price, amount, created) <>(VariantEntity.tupled, VariantEntity.unapply)
}

object Variants extends QueryForTableWithIdAsPrimaryKey[VariantEntity, Variants](TableQuery[Variants]) {

}