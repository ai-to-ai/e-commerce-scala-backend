package com.nichoshop.models.helpers

import com.nichoshop.Environment.driver.simple._

import scala.slick.lifted.{Query, Tag}

/**
 * Created by Evgeny Zhoga on 13.06.15.
 */
abstract class IdAsPrimaryKey {
  def id: Option[Int] = None
}

abstract class TableWithIdAsPrimaryKey[R <: IdAsPrimaryKey](tag:Tag, schema:String) extends Table[R](tag, schema) {
 def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
}

abstract class QueryForTableWithIdAsPrimaryKey[R <: IdAsPrimaryKey, T <: TableWithIdAsPrimaryKey[R] ]( val query: scala.slick.lifted.TableQuery[T] ) {
  type QueryType = Query[T,R,Seq]

  def findById(id: Int)(implicit s:Session): Option[R] = query.filter( _.id === id).firstOption(s)

  def findById(id:Option[Int])(implicit s:Session):Option[R] = id.flatMap( unpackedId => findById(unpackedId)(s) )

  def insert(obj:R)(implicit s:Session): Int = obj.id match {
    case Some(id) => 0
    case None => (query returning query.map(_.id)) += obj
  }

  def update(obj:R)(implicit s:Session):Int = obj.id match {
    case Some(id) => query.filter( _.id === id ).update( obj )
    case None => 0
  }

  def delete(id:Int)(implicit s:Session):Int = {
    query.filter( _.id === id ).delete
  }

}