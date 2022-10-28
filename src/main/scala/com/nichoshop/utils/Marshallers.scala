package com.nichoshop.utils

import com.nichoshop.model.dto._
import org.json4s._
import org.json4s.native.JsonMethods._
import org.scalatra._
import org.scalatra.{BadRequest, Forbidden, NotFound, Unauthorized, halt, Ok}
import com.nichoshop.Environment

import scala.collection.JavaConversions._
import scala.language.implicitConversions


/**
 * Created by Evgeny Zhoga on 04.06.15.
 * Modified by Nursultan on 7/3/2022.
 */
object Marshallers {

  private implicit def long2BigInt(v: Long): BigInt = BigInt(v)
  private implicit def javaInteger2BigInt(v: java.lang.Integer): BigInt = BigInt(v.intValue())
  private implicit def javaLong2BigInt(v: java.lang.Long): BigInt = BigInt(v.longValue())

  def toJson(user: UserDto): JValue = {
    JObject(List(
      "id" -> JInt(user.getId),
      "username" -> JString(user.getUserid),
      "firstName" -> JString(user.getFirstName),
      "lastName" -> JString(user.getLastName),
      "email" -> JString(user.getEmail),
      "phone" -> JString(user.getPhone),
      "phoneConfirmed" -> JString(user.getPhoneConfirmed.toString),
      "accountType" -> JInt(user.getAccountType),
      "roles" -> JArray(user.getRoles.map(_.name()).map(JString).toList)
    ))
  }

  def toJson(specificDto: SpecificDto): JValue = {
    JObject(List(
      "id" -> JInt(specificDto.getId),
      "name" -> JString(specificDto.getName),
      "defaultValue" -> JString(specificDto.getDefaultValue),
      "required" -> JBool(specificDto.getRequired)
    ))
  }

  def toJson(category: CategoryDto): JValue = {
    JObject(List(
      Some("name" -> JString(category.getName)),
      Some("id" -> JInt(category.getId.intValue())),
      Some("parentId" -> JInt(category.getParentId.intValue())),
      Some("conditions" -> JArray(category.getConditions.map(JString).toList)),
      // "specifics" -> JString(category.getSpecifics)
      if(category.getSpecifics == null) None
      else Some("specifics" -> JArray(category.getSpecifics.getOptions.map(s => toJson(s)).toList))
    ).flatten)
  }

  def toJson(tree: CategoryTreeDto): JValue = {
    if (tree.getCategory == Conversions.ROOT_CATEGORY) {
      JArray(
        tree.getChildren.map(toJson).toList
      )
    } else {
      JObject(List(
        Some("category" -> toJson(tree.getCategory)),
        if (tree.getChildren.isEmpty) None else Some("children" -> JArray(tree.getChildren.map(toJson).toList))
      ).flatten)
    }
  }

  def toJson(items: List[ItemDto]): JValue = {
    JArray( items.map{ case p: ItemDto  => toJson(p)} )
  }

  def toJson(item: ItemDto): JValue = {
    JObject(List(
      "id" -> JInt(item.getId.intValue()),
      "title" -> JString(item.getTitle),
      "description" -> JString(item.getDescription),
      "sellerId" -> JInt(item.getSellerId.intValue()),
      "categoryId" -> JInt(item.getCategoryId.intValue()),
      "variants" -> JArray(item.getVariants.map{case v: VariantDto => toJson(v)}.toList)
    ))
  }
  // def toJson(item: CartItemDto): JValue = {
  //   JObject(List(
  //     "item" -> toJson(item.getItem),
  //     "qty" -> JInt(item.getQty.intValue())
  //   ))
  // }

  def toJson(variant: VariantDto): JValue = {
    JObject(List(
      Some("id" -> JInt(variant.getId.intValue())),
      Some("title" -> JString(variant.getTitle)),
      Some("description" -> JString(variant.getDescription)),
      Some("price" -> JInt(variant.getPrice.getAmount.intValue())),
      Some("amount" -> JInt(variant.getAmount.intValue())),
      Some("condition" -> JString(variant.getCondition.name()))
    ).flatten)
  }

  def toJson(address: AddressDto): JValue = {
    JObject(List(
      Some("id" -> JInt(address.getId)),
      Some("name" -> JString(address.getName)),
      Some("userId" -> JInt(address.getUserId)),
      Some("address1" -> JString(address.getAddressLine1)),
      Some("address2" -> JString(address.getAddressLine2)),
      Some("city" -> JString(address.getCity)),
      Some("state" -> JString(address.getState)),
      Some("zip" -> JString(address.getZip)),
      Some("country" -> JString(address.getCountry)),
      Some("phone" -> JString(address.getPhone)),
      Some("addressType" -> JInt(address.getAddressType)),
      Some("status" -> JInt(address.getStatus))
    ).flatten)
  }

  def toJson(addresses: List[AddressDto],desc: String = "Address"): JValue = {
    JObject(List(
    Some("list" -> JArray( addresses.map{ case a: AddressDto  => toJson(a)} ))
    ).flatten)
  }

  def toJson(businessNAddress: Tuple2[BusinessDto, AddressDto]): JValue = {
    JObject(List(
      "name" -> JString(businessNAddress._1.getName),
      "address" -> Marshallers.toJson(businessNAddress._2),
      "vatCountry" -> JString(businessNAddress._1.getVatCountry),
      "vatNumber" -> JString(businessNAddress._1.getVatNumber)
      ))
  }
  def data(data: String) = {
    JObject(List(
            "status" -> JString("true"),
            "data" -> JString(data)
          ))
  }

  def ok(additions: Map[String, String] = Map.empty) = {
    val tuples: List[(String, JString)] =  ("status",JString("true")):: additions.toList.map{case (k, v) => k -> JString(v)}
    JObject( tuples)
    
  }

  def fail(message: String) = {
    Ok(body = JObject(List(
          "status" -> JString("false"),
          "error" -> JString(message)
        ))
    )
  }
  
  def unauthorized(message: String, additions: Map[String, String] = Map.empty) = {
    val tuples: List[(String, JString)] = ("error", JString(message)) :: additions.toList.map{case (k, v) => k -> JString(v)}
    halt(Unauthorized(body =JObject( tuples )))
  }

  def bad(message: String) = {
    halt(BadRequest(body = JObject(List(
          "error" -> JString(message)
        ))
    ))
  }

  def forbidden(message: String) = {
    halt(Forbidden(body = JObject(List(
          "error" -> JString(message)
        ))
    ))
  }
  def notFound(message: String) = {
    halt(NotFound(body = JObject(List(
          "error" -> JString(message)
        ))
    ))
  }

  def redirect(url : String = Environment.host) = {
  halt(status = 303, headers = Map("Location" -> url))
}

}
      