package com.nichoshop.utils

import com.nichoshop.legacy.models.{SessionsRow, UsersRow}
import com.nichoshop.model.dto._
import com.nichoshop.services.PermissionsService

import java.util.concurrent.TimeUnit

//.{Token => PublicToken, User => PublicUser, Session => PublicSession, Category => PublicCategory, Product => PublicProduct, ProductVariant => PublicProductVariant, Roles, CategoryTree}
import com.nichoshop.models

import scala.collection.JavaConversions._

/**
 * Methods to convert DB representation to serializable
 *
 * Created by Evgeny Zhoga on 03.06.15.
 */
object Conversions {
  import CaseClasses._
  //private val log = LoggerFactory.getLogger(getClass)
  @deprecated(message = "Use com.nichoshop.models.Session class for DB representation")
  def toSession(session: SessionsRow): SessionDto = toSession(session.userId, session.hash, session.creationTime)

  def toSession(session: models.SessionEntity): SessionDto = toSession(session.userId, session.hash, session.creationTime)

  def toSession(userId: String,
                 sessionId: String,
                  timestamp: Long): SessionDto = SessionDto.
    newBuilder().
    setId(sessionId).
    setUserid(userId).
    setTimestamp(timestamp).
    build

  def toToken(token: models.TokenEntity): TokenDto = TokenDto.newBuilder().
    setHash(token.hash).
    setHashSession(token.hashSession).
    setUserid(token.userid).
    setTimestamp(token.creationTime).
    setCreated(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(365)).
    build()

  def toUser(u: models.UserEntity, roles: List[models.RoleEntity]) = {
    UserDto.newBuilder().
      setId(u.id.get).
      setUserid(u.userid).
      setEmail(u.email).
      setEmailConfirmed(u.emailConfirmed).
      setFirstName(u.name).
      setLastName(u.lname).
      setRegistrationTimestamp(u.registrationDate).
      setSuspended(u.suspended).
      setPhoneConfirmed(u.phoneConfirmed).
      setAccountType(u.accountType).
      setPhone(u.phone.getOrElse(null)).
      setRoles(roles.flatMap {
      case models.RoleEntity(_, _, models.Roles.Type.admin) => Some(Role.ADMIN)
      case models.RoleEntity(_, _, models.Roles.Type.support) => Some(Role.SUPPORT)
      case models.RoleEntity(_, _, models.Roles.Type.user) => Some(Role.USER)
    }).build()
  }

  val ROOT_CATEGORY = CategoryDto.newBuilder().setId(0).setParentId(0).setName("ROOT").build()

  def toCategory(category: models.CategoryEntity) = {
    CategoryDto.newBuilder().
    setId(category.id.get).
    setName(category.name).
    setParentId(category.parentId).
    setConditions(category.conditions).
    setSpecifics(category.specifics.getOrElse(null)).build()

}
  def toCategoryTree(categories: List[(models.CategoryEntity, List[models.CategoryEntity])]) = {
    CategoryTreeDto.newBuilder().
      setCategory(ROOT_CATEGORY).
      setChildren(
        categories.map {case (parent, children) =>
          CategoryTreeDto.newBuilder().
            setCategory(toCategory(parent)).
            setChildren(
              children.map(c => CategoryTreeDto.newBuilder().setCategory(toCategory(c)).setChildren(List()).build())
            ).build()
        }
      ).build()
  }
  def toCategoryTree(categories: List[models.CategoryEntity], rootId: Int, level: Int = Constants.categoryDepth) = {
    val rootCategory =
      if (rootId == 0) Some(ROOT_CATEGORY)
      else categories.find(_.id.get == rootId).map(toCategory)

    rootCategory.map {root =>
      val parentToList = categories./*filter(_.id == root.getId).*/map(toCategory).groupBy(_.getParentId)

      def next(root: CategoryDto, level: Int): CategoryTreeDto = {
        val builder = CategoryTreeDto.newBuilder().
          setCategory(root)
        parentToList.get(root.getId) match {
          case Some(children) if level > 0=>
            builder.setChildren(children.map(c => next(c, level - 1)))
          case _ =>
            builder.setChildren(List())
        }
        builder.build()
      }

      next(root, level)
    }
  }

  def toOrderDto(orderEntity: models.OrderEntity, trackingEntity: models.TrackingEntity,items: List[models.ItemEntity], transactions: List[models.TransactionEntity] = List(), userEntity: models.UserEntity) = {
    // OrderDto.newBuilder()
    //         .setPayment(orderEntity.payment)
    //         .setStatus(orderEntity.status)
    //         .setDelivery(orderEntity.delivery)
    //         .setTracking(
    //           TrackingDto.newBuilder()
    //                       .setCarrier(trackingEntity.carrier)
    //                       .setNumber(trackingEntity.number)
    //                       .setStep(trackingEntity.step).build()
    //         )
    //         .setSellerName(userEntity.userid)
    //         .setSoldBySeller(orderEntity.soldBySeller)
    //         .setOrderNum(orderEntity.orderNum)
    //         .setCreated(orderEntity.created)
    //         .setItems(
    //             items.map(i =>toOrderItemDto)
    //         )
            // .build()
  }


  def toOrderItemDto(itemEntity: models.ItemEntity, variants: List[models.VariantEntity]) = {
      // OrderItemDto.newBuilder()
      //             .setTitle(itemEntity.title)
      //             .setCondition("condition")
      //             .setVariants(
      //                 variants.map( v =>
      //                   VariantDto.newBuilder()
      //                             .setTitle(v.title.getOrElse(""))
      //                             .setValue(v.value)
      //                             .build()
      //                 )
      //             )
      //             .setQuantity(itemEntity.quantity)
      //             // .setPrice(itemEntity.total)
      //             .build()
  }

  def toOrderDetailDto(itemEntity:models.ItemEntity,trackingEntity: models.TrackingEntity,items: List[models.ItemEntity], transactions: List[models.TransactionEntity] = List(), userEntity: models.UserEntity) ={
    
  }

  def toSpecificDto(specific: SpecificClass) : SpecificDto = {
      SpecificDto.newBuilder()
                .setId(specific.id)
                .setName(specific.name)
                .setDefaultValue(specific.defaultValue)
                .setRequired(specific.required)
                .build()
  }
  def toSpecificArrayDto(specifics: List[SpecificClass]) : SpecificArrayDto = {

    SpecificArrayDto.newBuilder()
                    .setOptions(specifics.map(toSpecificDto))
                    .build()
  }

  // def toSpecificsString(specifics: )


}
