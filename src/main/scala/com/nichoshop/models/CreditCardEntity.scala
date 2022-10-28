package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 6/9/2022
 */
case class CreditCardEntity(

            override val id: Option[Int] = None,
            userId: Int, 
            holder: String, 
            cardType: Int, 
            number: Long, 
            month: Int, 
            year: Int, 
            code: String,
            addressId: Int,
            status: Boolean = false

 ) extends IdAsPrimaryKey

class CreditCards(tag:Tag) extends TableWithIdAsPrimaryKey[CreditCardEntity](tag, "creditcards") {

    def userId = column[Int]("user_id")
    def holder = column[String]("hoder") 
    def cardType = column[Int]("card_type")
    def number = column[Long]("number")
    def month = column[Int]("month")
    def year = column[Int]("year")
    def code = column[String]("code")
    def addressId = column[Int]("address_id")
    def status = column[Boolean]("status")

    def * = (id.?, userId, holder, cardType, number, month, year, code, addressId, status) <> (CreditCardEntity.tupled, CreditCardEntity.unapply)

}

object CreditCards extends QueryForTableWithIdAsPrimaryKey[CreditCardEntity, CreditCards](TableQuery[CreditCards]){
    
}