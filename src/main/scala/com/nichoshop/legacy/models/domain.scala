package com.nichoshop.legacy.models

case class AddressesRow(id: Int, userId: Int, address1: String, address2: Option[String] = None, city: String, state: String, zip: String, country: String, phones: String, addressType: Int = 0, status: Int = 0, name: String)
case class AdminSettingsRow(auctionEnabled: Boolean, disabledCountries: String, password: String)
case class BidRetractionsRow(id: Int, itemId: Int, userId: Int, problem: Byte, requested: Long, retracted: Boolean)
case class BidsRow(id: Int, bidderId: Int, productId: Int, price: Int, auto: Boolean, timestamp: Long)
case class CaseChangesRow(id: Int, caseId: Int, helpOption: Byte, refund: Option[Int] = None, timestamp: Long)
case class CaseMessagesRow(id: Int, caseId: Int, senderId: Int, message: String, timestamp: Long)
case class CasesRow(id: Int, sellId: Int, openedById: Int, openedAgainstId: Int, problem: Byte, message: String, opened: Long, closed: Boolean, itemProblem: Option[Byte] = None, helpOption: Option[Byte] = None, phone: Option[String] = None, refund: Option[Int] = None)
case class CategoriesRow(id: Int, name: String, leaf: Boolean, parentId: Int = 0, ord: Int = 0)
case class CategoryFieldsRow(id: Int, categoryId: Int, name: String)
case class CategoryFieldsValuesRow(id: Int, categoryFieldId: Int, name: String)
case class ChatMessagesRow(sessionId: Int, message: String, timestamp: Option[Long] = None)
case class ChatSessionsRow(id: Int, userId: Int, created: Long)
case class CreditCardsRow(id: Int, userId: Int, holder: String, cardType: Byte, number: Long, month: Byte, year: Short, code: Short)
case class DisabledIpsRow(ip: String)
case class DonationsRow(id: Int, userId: Int, amount: Int, timestamp: Long, profileId: Option[String] = None)
case class EmailConfirmationRow(userId: Int, code: String)
case class FeedbackRow(id: Int, sellerId: Int, sellId: Int, rating: Byte, itemAsDescribed: Byte, communication: Byte, shippingTime: Byte, shippingCharges: Byte, message: Option[String] = None, timestamp: Long)
case class GiftCardOrdersRow(id: Int, buyerId: Int, amount: Int, senderName: String, message: String, recipients: String, timestamp: Long, transactionId: String, deliveryDate: Option[Long] = None)
case class GiftCardsRow(id: Int, code: String, orderId: Int, activatorId: Option[Int] = None, email: String, delivered: Boolean, activationDate: Option[Long] = None)
case class MessageFoldersRow(id: Int, name: String, userId: Int)
case class MessagesRow(id: Int, fromId: Option[Int] = None, fromUserid: Option[String] = None, toId: Int, subject: String, message: String, creationTime: Long, flag: Boolean, msgRead: Boolean, itemId: Option[Int] = None, itemTitle: Option[String] = None, folderId: Int = 0)
case class OffersRow(id: Int, userId: Int, productId: Int, price: Int, quantity: Short, accepted: Boolean, paid: Boolean, offerTime: Long)
case class PasswordRecoveryRow(userId: Int, code: Option[String] = None)
case class PaypalAccountsRow(id: Int, userId: Int, email: String, primaryAcc: Boolean)
case class ProductFieldValuesRow(id: Int, fieldId: Int, productId: Int, value: String)
case class ProductViewsRow(productId: Int, timestamp: Long)
case class ScheduledPaymentsRow(id: Int, fromId: Int, toId: Int, amount: Int, paymentDate: Long, delivered: Boolean, payKey: Option[String] = None)
case class SellsRow(id: Int, buyerId: Int, productId: Int, quantity: Short, price: Int, paid: Boolean, dispatched: Boolean, soldTime: Long, sellerId: Int)
case class SessionsRow(id: Int, userId: String, hash: String, creationTime: Long)
case class SuggestionsRow(id: Int, userId: Option[Int] = None, topic: Byte, message: String, timestamp: Long)
case class TellUsRow(id: Int, message: String, timestamp: Long, ip: String)
case class TokensRow(id: Int, userid: String, hash: String, creationTime: Long)
case class TrackingRow(id: Int, sellId: Int, number: String, courier: String)
case class UsersRow(id: Int, userid: String, password: String, email: String, name: String, lname: String, registrationDate: Long, emailConfirmed: Boolean = false, suspended: Boolean = false, deleted: Boolean = false, deletedDate: Option[Long] = None, giftCardBalance: Int = 0, question: Option[String] = None, answer: Option[String] = None, registrationAddressId: Option[Int] = None, fromAddressId: Option[Int] = None, toAddressId: Option[Int] = None, returnAddressId: Option[Int] = None, paymentAddressId: Option[Int] = None, phone: Option[String] = None, accountType: Option[String] = None)
case class WatchlistRow(id: Int, userId: Int, productId: Int)
case class ProductsRow(id: Int, sellerId: Int, catId: Int, title: String, subtitle: String, itemCondition: Byte, images: String, fixedPrice: Boolean, startingPrice: Int, nowPrice: Option[Int] = None, duration: Int, location: String, creationTime: Long, state: Byte = 0, productDetails: ProductDetailsRow)
case class ProductDetailsRow(description: String, conditionDesc: Option[String] = None, quantity: Short = 1, credit: Boolean = true, paypal: Boolean = false, bitcoin: Boolean = false, gift: Boolean = false, bestOffer: Boolean = false, returns: Boolean = false, returnsDetails: Option[String] = None, domesticService: Byte = 1, domesticCost: Option[Int] = None, domesticCollect: Boolean = false, internationalService: Option[Byte] = None, internationalCost: Option[Int] = None, postTo: Option[String] = None)
case class PurchaseRow(sell: SellsRow, trackingNumber: Option[String], toAddress: String, fromAddress: String, title: String, images: String, internationalCost: Option[Int], domesticCost: Option[Int], sellerId: Int, sellerName: String, positiveFeeds: Int = 0, negativeFeeds: Int = 0, feedbackId: Option[Int]) {

// case class SUCConfirmationRow(userId: Int, code: String)
// case class UserAddressRow(id: Int, address_id: Int, user_id: Int, address_type: Int, status: Int)

  def score = positiveFeeds + negativeFeeds
  def scoreRate = if (score > 0) positiveFeeds.toDouble / score else 0.0
  def totalPrice = (sell.price + shippingPrice) * sell.quantity

  private def shippingPrice = (if (toAddress == fromAddress) domesticCost else internationalCost) getOrElse 0
}

object Manifests {
  def user = manifest[UsersRow]
  def category = manifest[CategoriesRow]
  def product = manifest[ProductsRow]
  def purchase = manifest[Purchase]
  def message = manifest[MessagesRow]
}
