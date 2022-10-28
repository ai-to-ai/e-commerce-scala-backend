package com.nichoshop.swaggers

import org.scalatra.swagger._

trait ApiDescription[T] extends SwaggerSupport {

  def name: String

  implicit def manifestForT: Manifest[T]

  protected def applicationDescription = s"Operations on $name"

  val getAll =
    (apiOperation[List[T]]("findAll")
      summary s"Get all ${name}s"
      notes s"Get all ${name}s")

  val getById =
    (apiOperation[Option[T]]("getById")
      summary s"Find $name by id"
      notes s"Find $name by id"
      parameter pathParam[Int]("id").description(s"An id to search for"))

  val create =
    (apiOperation[Unit]("create")
      summary s"Create $name"
      notes s"Create $name"
      parameter bodyParam[T].description(s"$name object"))

  val deleteById =
    (apiOperation[Unit]("deleteById")
      summary s"Delete $name by id"
      notes s"Delete $name by id"
      parameter pathParam[Int]("id").description(s"An id to delete"))

  val updateById =
    (apiOperation[Unit]("updateById")
      summary s"Update $name by id"
      notes s"Update $name by id"
      parameters(bodyParam[T].description(s"$name object"),
      pathParam[Int]("id").description(s"An id to update")))

}
