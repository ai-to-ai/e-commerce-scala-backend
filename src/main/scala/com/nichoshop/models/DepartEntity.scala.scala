
package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import scala.slick.lifted.Tag

/**
 * Created by Nursultan on 7/6/2022
 */
case class DepartEntity(

            override val id: Option[Int] = None,
            name: String,
            created: Long = System.currentTimeMillis()
            
) extends IdAsPrimaryKey

class Departs(tag:Tag) extends TableWithIdAsPrimaryKey[DepartEntity](tag, "departments") {
  
  def name = column[String]("name")
  def created = column[Long]("created")

  def * = ( id.?, name, created) <> ( DepartEntity.tupled, DepartEntity.unapply )

}

object Departs extends QueryForTableWithIdAsPrimaryKey[DepartEntity, Departs](TableQuery[Departs]) {

}