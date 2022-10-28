package com.nichoshop.legacy.dao

import com.nichoshop.legacy.models._


trait GenericDAO[T] {
  def findById(id:Int): Option[T]
  def findAll: List[T]
  def create(x: T): Unit
  def updateById(id: Int, x: T): Unit
  def deleteById(id: Int): Unit
}

trait UserDAO extends GenericDAO[UsersRow] {
  def saveEmailConfirmationCode(emailConfirmationRow: EmailConfirmationRow): Unit
  def confirmEmail(code: String): Boolean
  def toToken(user: UsersRow): Option[TokensRow]
  def fromToken(hash: String): Option[UsersRow]
  def fromSession(s: String): Option[UsersRow]
  def toSession(userid: String): Option[SessionsRow]
  def findByEmail(email: String): Option[UsersRow]
  def findByPhone(phone: String): Option[UsersRow]
  def updateByUserId(user: UsersRow): Unit
  def findByUserId(userid: String): Option[UsersRow]
  def deleteByUserId(userid: String): Unit
}

trait SessionDAO extends GenericDAO[SessionsRow] {
  def deleteByHash(hash: String): Unit
}

trait TokenDAO extends GenericDAO[TokensRow] {
  def deleteByHash(hash: String): Unit
  def findByHash(hash: String):Option[TokensRow]
  def deleteByUserId(userid: String): Unit
  def findByUserId(userid: String): Option[TokensRow]
}

trait CategoryDAO extends GenericDAO[CategoriesRow] {
  def subcategories(id: Int): List[CategoriesRow]
  def sidebarCategories: List[CategoriesRow]
}

trait ProductDAO extends GenericDAO[ProductsRow] {
  def search(s: String): List[ProductsRow]
  def filterByCategory(catId: Int): List[ProductsRow]
  def filterByState(sellerId: Int, state: Byte, filter: Int, from: Int, size: Int): List[ProductsRow]
  def searchWithCategoryId(catId: Int, str: String): List[ProductsRow]
  def randomWithCategory(quantity: Int, catId: Int): List[ProductsRow]
  def deleteMarked(sellerId: Int, ids: List[Int]): Unit
  def watchlist(userId: Int, from: Int, size: Int): List[ProductsRow]
}

trait PurchaseDAO {
  def findAll(userId: Int, from: Int, size: Int): List[PurchaseRow]
}

trait MessageDAO {
  def findById(userId: Int, mid: Int): Option[MessagesRow]
  def deleteById(userId: Int, mid: Int): Unit
  def getAllByUserId(id: Int, from: Int, size: Int): List[MessagesRow]
  def fromNichoShop(id: Int, from: Int, size: Int): List[MessagesRow]
  def fromMembers(id: Int, from: Int, size: Int): List[MessagesRow]
  def highPriority(id: Int, from: Int, size: Int): List[MessagesRow]
  def fromFolder(id: Int, folderId: Int, from: Int, size: Int): List[MessagesRow]
  def moveToFolder(userId: Int, ids: List[Int], folderId: Int): Unit
  def markAsRead(userId: Int, ids: List[Int]): Unit
  def markAsUnread(userId: Int, ids: List[Int]): Unit
  def markAsFlagged(userId: Int, ids: List[Int]): Unit
  def markAsUnflagged(userId: Int, ids: List[Int]): Unit
  def searchByKeyword(userId: Int, keyword: String, searchSpace: Int, from: Int, size: Int): List[MessagesRow]
  def searchBySenderName(userId: Int, senderName: String, searchSpace: Int, from: Int, size: Int): List[MessagesRow]
  def searchByItemId(userId: Int, itemId: Int, searchSpace: Int, from: Int, size: Int): List[MessagesRow]
}

trait MessageFolderDAO {
  def deleteById(userId: Int, id: Int): Unit
  def getAllByUserId(userId: Int): List[MessageFoldersRow]
  def create(x: MessageFoldersRow): Unit
}

trait SellDAO {
  def markAsDispatched(sellerId: Int, sellId: Int): Unit
  def findById(id: Int): Option[SellsRow]
}

trait TrackingDAO {
  def create(t: TrackingRow): Unit
}

trait AddressDAO {
  def findByUserId(userId: Int) : List[AddressesRow]
  def create(x: AddressesRow): Unit
  def findByAddressType(userId: Int, addressType: Int) : List[AddressesRow]
  def updateById(id: Int, address: AddressesRow): Unit
  def updateStatus(id: Int, status: Int) : Unit
  def deleteById(id: Int) : Unit
}