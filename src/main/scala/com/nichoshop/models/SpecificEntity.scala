package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._
import com.nichoshop.model.dto.AttributeOptionsDto


import scala.slick.lifted.Tag

/**
 * Created by Nursultan on 7/1/2022.
 */
case class SpecificEntity(
            override val id: Option[Int] = None,
            name: String,
            valueType: String = "string",
            valueOptions: Option[ AttributeOptionsDto ] = None,
            required: Boolean = false,
            defaultValue: Option[String] = None,
            created: Long = System.currentTimeMillis()
) extends IdAsPrimaryKey
object SpecificEntityImplicits {
  implicit val schema = AttributeOptionsDto.getClassSchema
  implicit val avroMapper = avroBasedColumnType[AttributeOptionsDto]
}

class Specifics(tag:Tag) extends TableWithIdAsPrimaryKey[SpecificEntity](tag, "specifics") {
  import SpecificEntityImplicits._

  def name = column[String]("name")
  def valueType = column[String]("value_type")
  def valueOptions = column[Option[AttributeOptionsDto]]("value_options")
  def required = column[Boolean]("required")
  def defaultValue = column[Option[String]]("default_value")
  def created = column[Long]("created")

  def * = (id.?, name, valueType, valueOptions, required, defaultValue, created) <>(SpecificEntity.tupled, SpecificEntity.unapply)
}


object Specifics extends QueryForTableWithIdAsPrimaryKey[SpecificEntity, Specifics](TableQuery[Specifics]){
  object ValueTypes {
    val string = "string"
    val integer = "int"
  }
}