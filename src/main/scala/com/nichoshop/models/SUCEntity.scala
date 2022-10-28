package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import scala.slick.lifted.Tag

/**
 * Created by Nurusultan on 5/20/2022.
 */
case class SUCEntity(

            override val id: Option[Int] = None,
            userId: Int,
            code: String,
            sucType: Int = 0,
            created: Long = System.currentTimeMillis()
            
) extends IdAsPrimaryKey

class SUCs(tag:Tag) extends TableWithIdAsPrimaryKey[SUCEntity](tag, "sucs") {

    def userId = column[Int]("user_id")
    def code = column[String]("code")
    def sucType = column[Int]("suc_type")
    def created = column[Long]("created")

    def * = ( id.?, userId, code, sucType, created) <> ( SUCEntity.tupled, SUCEntity.unapply )
}

object SUCs extends QueryForTableWithIdAsPrimaryKey[SUCEntity, SUCs](TableQuery[SUCs]) {

}
