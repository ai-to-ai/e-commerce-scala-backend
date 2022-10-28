package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import scala.slick.lifted.Tag

/**
 * Created by Nursultan on 7/18/2022.
 */
case class BankAccountEntity(

            override val id: Option[Int] = None,
            userId: Int,
            accountType: Int,
            country: String,
            street: String,
            apartment: String,
            city: String,
            state: String,
            zip: String,
            holderName: String,
            iban: String = "",
            bic: String = "",
            sortCode: String = "",
            digitRoutingNumber: String = "",
            branchCode: String = "",
            insituationNumber: String = "",
            bankName: String = "",
            accountNumber: String = "",
            currency: String = "usd"
            
) extends IdAsPrimaryKey

class BankAccounts (tag:Tag) extends TableWithIdAsPrimaryKey[BankAccountEntity](tag, "bank_accounts") {
  def userId = column[Int]("user_id")
  def accountType = column[Int]("account_type")
  def country = column[String]("country")
  def street = column[String]("street")
  def apartment = column[String]("apartment")
  def city = column[String]("city")
  def state = column[String]("state")
  def zip = column[String]("zip")
  def holderName = column[String]("holder_name")

  //IBAN
  def iban = column[String]("iban")

  //IBAN & Other
  def bic = column[String]("bic")

  //GB
  def sortCode = column[String]("sort_code")

  //US
  def digitRoutingNumber = column[String]("digit_routing_number")

  //CA
  def branchCode = column[String]("branch_code")
  def insituationNumber = column[String]("insituation_number")
  def bankName = column[String]("bank_name")

  //GB & US & CA & Other
  def accountNumber = column[String]("account_number")

  def currency = column[String]("currency")

  def * = ( id.?, userId, accountType, country, street, apartment, city, state, zip, holderName, iban, bic, sortCode, digitRoutingNumber, branchCode, insituationNumber, bankName, accountNumber, currency) <> ( BankAccountEntity.tupled, BankAccountEntity.unapply )

}

object BankAccounts extends QueryForTableWithIdAsPrimaryKey[BankAccountEntity, BankAccounts](TableQuery[BankAccounts]) {
}