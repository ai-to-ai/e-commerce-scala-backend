package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 7/6/2022
 */

case class CSEntity(

            override val id: Option[Int] = None,
            username: String,
            fname: String,
            lname: String,
            password: String,
            email: String,
            phone: String,
            depart: Int,
            subDepart: Int,
            timezone: String,
            contact: Boolean,
            monToFri: Int,
            saturday: Int,
            sunday: Int,
            currencyId: Int,
            created: Long = System.currentTimeMillis()

) extends IdAsPrimaryKey

class CSs(tag:Tag) extends TableWithIdAsPrimaryKey[CSEntity](tag, "customer_supports") {

    def username = column[String]("username")
    def fname= column[String]("fname")
    def lname = column[String]("lname")
    def password = column[String]("password")
    def email = column[String]("email")
    def phone = column[String]("phone")
    def depart = column[Int]("depart")
    def subDepart = column[Int]("sub_depart")
    def timezone = column[String]("timezone")
    def contact = column[Boolean]("contact")
    def monToFri = column[Int]("mon_to_fri")
    def saturday = column[Int]("saturday")
    def sunday = column[Int]("sunday")
    def currencyId = column[Int]("currency_id")
    def created = column[Long]("created")

  def * = (id.?, username, fname, lname, password, email, phone, depart, subDepart, timezone, contact, monToFri, saturday, sunday, currencyId, created) <> (CSEntity.tupled, CSEntity.unapply)
}

object CSs extends QueryForTableWithIdAsPrimaryKey[CSEntity, CSs](TableQuery[CSs]) {
}