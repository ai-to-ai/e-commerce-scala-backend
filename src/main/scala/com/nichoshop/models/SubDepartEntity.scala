
package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

import scala.slick.lifted.Tag

/**
 * Created by Nursultan on 7/6/2022
 */
case class SubDepartEntity(

            override val id: Option[Int] = None,
            name: String,
            created: Long = System.currentTimeMillis()
            
) extends IdAsPrimaryKey

class SubDeparts(tag:Tag) extends TableWithIdAsPrimaryKey[SubDepartEntity](tag, "sub_departments") {
  
  def name = column[String]("name")
  def created = column[Long]("created")

  def * = ( id.?, name, created) <> ( SubDepartEntity.tupled, SubDepartEntity.unapply )

}

object SubDeparts extends QueryForTableWithIdAsPrimaryKey[SubDepartEntity, SubDeparts](TableQuery[SubDeparts]) {

}