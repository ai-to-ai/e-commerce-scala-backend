package com.nichoshop.utils

import com.nichoshop.model.dto._
import com.nichoshop.models

import scala.collection.JavaConversions._
import scala.language.implicitConversions
import com.nichoshop.legacy.models.{AddressesRow, UsersRow}


/**
 * Created by Evgeny Zhoga on 12.10.15.
 */
object Converters {
  implicit def user2User(uANDp: (models.UserEntity,List[models.RoleEntity])): UserDto = Conversions.toUser(uANDp._1, uANDp._2)

  implicit def rm2richMoney(rm: models.helpers.RichMoney): RichMoneyDto =
    RichMoneyDto.newBuilder().
      setCurrencyId(rm.currencyId).
      setAmount(rm.amount).
      build()

  implicit def richModey2rm(rm: RichMoneyDto): models.helpers.RichMoney =
    models.helpers.RichMoney(
      rm.getAmount,
      rm.getCurrencyId
    )
  implicit def business2business(business: models.BusinessEntity): BusinessDto = 
    BusinessDto.newBuilder()
              .setId(business.id.get)
              .setAddressId(business.addressId.getOrElse(0))
              .setName(business.name)
              .setVatCountry(business.vatCountry)
              .setVatNumber(business.vatNumber)
              .build()

  implicit def address2address(address: models.AddressEntity): AddressDto =
    AddressDto.newBuilder().
      setAddressIsVerified(address.addressIsVerified).
      setAddressLine1(address.address1).
      setAddressLine2(address.address2.orNull).
      setCity(address.city).
      setCountry(address.country).
      setId(address.id.get).
      setPhone(address.phone).
      setState(address.state.orNull).
      setUserId(address.userId).
      setZip(address.zip).
      setStatus(address.status).
      setAddressType(address.addressType).
      setName(address.name).
      build()

    implicit def variant2variantBuilder(variant: models.VariantEntity): VariantDto.Builder = {
      val v = VariantDto.newBuilder().
        setCreated(variant.created).
        setAmount(variant.amount).
        setPrice(RichMoneyDto.newBuilder().setAmount(variant.price.amount).setCurrencyId(variant.price.currencyId).build)

      variant.id.foreach(v.setId( _ ))
      v
    }

   implicit def item2itemBuilder(item: models.ItemEntity): ItemDto.Builder = {
    val itemBuilder = ItemDto.newBuilder().
      setCreated(item.created).
      setCategoryId(item.catId).
      setDescription("").
      setSellerId(item.sellerId).
      setTitle(item.title)
    item.id.foreach(itemBuilder.setId( _ ))
    itemBuilder
  }

  implicit def iv2item(itemAndVariant: Tuple2[models.ItemEntity, models.VariantEntity]): ItemDto =
    isv2item( (itemAndVariant._1, Seq(itemAndVariant._2)) )

  implicit def isv2item(itemAndVariant: Tuple2[models.ItemEntity, Seq[models.VariantEntity]]): ItemDto = {
      val item: ItemDto.Builder = itemAndVariant._1
      val v = itemAndVariant._2.map(v =>
        v.setDescription(v.description.getOrElse(item.getDescription)).
        setTitle(v.title.getOrElse(item.getTitle)).
        build())
      
      item.setVariants(seqAsJavaList(v)).
        build()
    }
    
  implicit def order2orderBuilder(order: models.OrderEntity): SellerOrderDto.Builder = {
      val orderBuilder = SellerOrderDto.newBuilder().
        setSoldDate(order.soldDate.getOrElse(0)).
        setOrderId(order.orderId).
        setBuyername(order.buyerName).
        setStatus(order.status)
      order.id.foreach(orderBuilder.setId( _ ))
      orderBuilder
    }
 

  implicit def c2condition(condition: Condition): String = condition match {
    case Condition.NEW => Constants.Condition.`new`
    case Condition.USED => Constants.Condition.used
  }

  implicit def so2sellerOrderItemEntity(sellerOrder: Tuple2[models.OrderEntity, Seq[models.ItemEntity]]) : SellerOrderDto = {
    val items = sellerOrder._2.map(i=>i:ItemDto.Builder)
    var orderDtoBuilder = sellerOrder._1 : SellerOrderDto.Builder
    orderDtoBuilder.setItems(seqAsJavaList(items.map(i => i.build()))).build()
  }

  implicit def so2sellerOrderItemDto(sellerOrder: Tuple2[models.OrderEntity, ItemDto]) : SellerOrderDto = {
    so2sellerOrderItemsDto( (sellerOrder._1, Seq(sellerOrder._2)) )
  }

  implicit def so2sellerOrderItemsDto(sellerOrder: Tuple2[models.OrderEntity, Seq[ItemDto]]) : SellerOrderDto = {
    val items = sellerOrder._2
    var orderDtoBuilder = sellerOrder._1 : SellerOrderDto.Builder
    orderDtoBuilder.setItems(seqAsJavaList(items)).build()
  }

  implicit def order2orderDetailBuilder(order: models.OrderEntity): OrderDetailDto.Builder = {
      val orderBuilder = OrderDetailDto.newBuilder().
        setPayment(order.payment).
        setStatus(order.status).
        setDelivery(order.delivery)
      order.id.foreach(orderBuilder.setId( _ ))
      orderBuilder
    }
  
}
