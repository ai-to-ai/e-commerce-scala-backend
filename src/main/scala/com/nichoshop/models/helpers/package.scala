package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.utils.Memcached
import com.nichoshop.model.dto.SpecificDto

import scala.concurrent.duration.FiniteDuration
import scala.language.implicitConversions
import scala.reflect.ClassTag

/**
 * Created by Evgeny Zhoga on 21.09.15.
 */
package object helpers {
  case class RichMoney(amount: Int, currencyId: Int = Currencies.EURO.code) {
    require( Currencies.byCode(currencyId).isDefined )

    private val currency = Currencies.byCode(currencyId).get
    val high = amount / currency.base
    val low = amount % currency.base
  }

  def avroBasedColumnType[T: ClassTag](implicit schema: org.apache.avro.Schema) =
    MappedColumnType.base[T, Array[Byte]](
      jsonable => {
        Memcached.toBytes( jsonable, schema )
      },
      str => {
        Memcached.fromBytes[T](str, schema)
      }
    )

  implicit def duration2Long(d: FiniteDuration): Long = d.toMillis

  def separatedString(separator: Char = ',') =
    MappedColumnType.base[List[String], String](
      list => if (list.isEmpty) "" else list.mkString("" + separator),
      s => if (s == "") List() else s.split(separator).toList
    )
  
  def separatedInt(separator: Char = ',') =
    MappedColumnType.base[List[Int], String](
      list => if (list.isEmpty) "" else list.mkString("" + separator),
      s => if (s == "") List() else s.split(separator).toList.map(_.toInt)
    )
}
