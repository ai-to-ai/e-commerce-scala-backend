package com.nichoshop.legacy.slick

import com.nichoshop.legacy.models._

/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = com.nichoshop.Environment.driver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: scala.slick.driver.JdbcProfile

  import profile.simple._

  import scala.slick.collection.heterogenous._
  import scala.slick.model.ForeignKeyAction

  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.

  import scala.slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val ddl = Addresses.ddl ++ AdminSettings.ddl ++ BidRetractions.ddl ++ Bids.ddl ++ CaseChanges.ddl ++ CaseMessages.ddl ++ Cases.ddl ++ Categories.ddl ++ CategoryFields.ddl ++ CategoryFieldsValues.ddl ++ ChatMessages.ddl ++ ChatSessions.ddl ++ CreditCards.ddl ++ DisabledIps.ddl ++ Donations.ddl ++ EmailConfirmation.ddl ++ Feedback.ddl ++ GiftCardOrders.ddl ++ GiftCards.ddl ++ MessageFolders.ddl ++ Messages.ddl ++ Offers.ddl ++ PasswordRecovery.ddl ++ PaypalAccounts.ddl ++ ProductFieldValues.ddl ++ Products.ddl ++ ProductViews.ddl ++ ScheduledPayments.ddl ++ Sells.ddl ++ Suggestions.ddl ++ TellUs.ddl ++ Tracking.ddl ++ Users.ddl ++ Watchlist.ddl ++ Sessions.ddl ++ Tokens.ddl

  implicit def GetResultAddressesRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[String], e3: GR[Option[String]]): GR[AddressesRow] = GR {
    prs => import prs._
      AddressesRow.tupled((<<[Int], <<[Int], <<[String], <<?[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[Int], <<[Int], <<[String]))
  }
  /** Table description of table addresses. Objects of this class serve as prototypes for rows in queries. */
  class Addresses(_tableTag: Tag) extends Table[AddressesRow](_tableTag, "addresses") {
    def * = (id, userId, address1, address2, city, state, zip, country, phones, addressType, status, name) <>(AddressesRow.tupled, AddressesRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userId.?, address1.?, address2, city.?, state.?, zip.?, country.?, phones.?, addressType.?, status.?, name.?).shaped.<>({ r => import r._; _1.map(_ => AddressesRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id DBType(INT UNSIGNED) */
    val userId: Column[Int] = column[Int]("user_id")
    /** Database column credit_card_id DBType(INT UNSIGNED), Default(None) */
    // val creditCardId: Column[Option[Int]] = column[Option[Int]]("credit_card_id", O.Default(None))
    // /** Database column contact_name DBType(VARCHAR), Length(33,true) */
    // val contactName: Column[String] = column[String]("contact_name", O.Length(33, varying = true))
    /** Database column address1 DBType(VARCHAR), Length(32,true) */
    val address1: Column[String] = column[String]("address1", O.Length(32, varying = true))
    /** Database column address2 DBType(VARCHAR), Length(32,true), Default(None) */
    val address2: Column[Option[String]] = column[Option[String]]("address2", O.Length(32, varying = true), O.Default(None))
    /** Database column city DBType(VARCHAR), Length(16,true) */
    val city: Column[String] = column[String]("city", O.Length(16, varying = true))
    /** Database column state DBType(VARCHAR), Length(16,true) */
    val state: Column[String] = column[String]("state", O.Length(16, varying = true))
    /** Database column zip DBType(VARCHAR), Length(16,true) */
    val zip: Column[String] = column[String]("zip", O.Length(16, varying = true))
    /** Database column country DBType(CHAR), Length(2,false) */
    val country: Column[String] = column[String]("country", O.Length(2, varying = false))
    /** Database column phone DBType(VARCHAR), Length(16,true) */
    val phones: Column[String] = column[String]("phones", O.Length(16, varying = true))
    
    /** Database column address_type DBType(Int UNSIGNED) Default(0) */
    val addressType: Column[Int] = column[Int]("address_type", O.Default(0))
    /** Database column status DBType(Int UNSIGNED) Default(0) */
    val status: Column[Int] = column[Int]("status", O.Default(0))
    /** Database column name DBType(Int UNSIGNED) Default(0) */
    val name: Column[String] = column[String]("name", O.Length(32, varying = true))

    /** Foreign key referencing CreditCards (database name addresses_ibfk_2) */
    // lazy val creditCardsFk = foreignKey("addresses_ibfk_2", creditCardId, CreditCards)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name addresses_ibfk_1) */
    lazy val usersFk = foreignKey("addresses_ibfk_1", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table Addresses */
  lazy val Addresses = new TableQuery(tag => new Addresses(tag))

  implicit def GetResultAdminSettingsRow(implicit e0: GR[Boolean], e1: GR[String]): GR[AdminSettingsRow] = GR {
    prs => import prs._
      AdminSettingsRow.tupled((<<[Boolean], <<[String], <<[String]))
  }

  /** Table description of table admin_settings. Objects of this class serve as prototypes for rows in queries. */
  class AdminSettings(_tableTag: Tag) extends Table[AdminSettingsRow](_tableTag, "admin_settings") {
    def * = (auctionEnabled, disabledCountries, password) <>(AdminSettingsRow.tupled, AdminSettingsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (auctionEnabled.?, disabledCountries.?, password.?).shaped.<>({ r => import r._; _1.map(_ => AdminSettingsRow.tupled((_1.get, _2.get, _3.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column auction_enabled DBType(BIT) */
    val auctionEnabled: Column[Boolean] = column[Boolean]("auction_enabled")
    /** Database column disabled_countries DBType(TEXT), Length(65535,true) */
    val disabledCountries: Column[String] = column[String]("disabled_countries", O.DBType("TEXT"))
    /** Database column password DBType(CHAR), Length(32,false) */
    val password: Column[String] = column[String]("password", O.Length(32, varying = false))
  }

  /** Collection-like TableQuery object for table AdminSettings */
  lazy val AdminSettings = new TableQuery(tag => new AdminSettings(tag))

  implicit def GetResultBidRetractionsRow(implicit e0: GR[Int], e1: GR[Byte], e2: GR[Long], e3: GR[Boolean]): GR[BidRetractionsRow] = GR {
    prs => import prs._
      BidRetractionsRow.tupled((<<[Int], <<[Int], <<[Int], <<[Byte], <<[Long], <<[Boolean]))
  }

  /** Table description of table bid_retractions. Objects of this class serve as prototypes for rows in queries. */
  class BidRetractions(_tableTag: Tag) extends Table[BidRetractionsRow](_tableTag, "bid_retractions") {
    def * = (id, itemId, userId, problem, requested, retracted) <>(BidRetractionsRow.tupled, BidRetractionsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, itemId.?, userId.?, problem.?, requested.?, retracted.?).shaped.<>({ r => import r._; _1.map(_ => BidRetractionsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column item_id DBType(INT UNSIGNED) */
    val itemId: Column[Int] = column[Int]("item_id")
    /** Database column user_id DBType(INT UNSIGNED) */
    val userId: Column[Int] = column[Int]("user_id")
    /** Database column problem DBType(TINYINT UNSIGNED) */
    val problem: Column[Byte] = column[Byte]("problem")
    /** Database column requested DBType(BIGINT UNSIGNED) */
    val requested: Column[Long] = column[Long]("requested")
    /** Database column retracted DBType(BIT) */
    val retracted: Column[Boolean] = column[Boolean]("retracted")

    /** Foreign key referencing Products (database name bid_retractions_ibfk_1) */
    lazy val productsFk = foreignKey("bid_retractions_ibfk_1", itemId, Products)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name bid_retractions_ibfk_2) */
    lazy val usersFk = foreignKey("bid_retractions_ibfk_2", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table BidRetractions */
  lazy val BidRetractions = new TableQuery(tag => new BidRetractions(tag))

  implicit def GetResultBidsRow(implicit e0: GR[Int], e1: GR[Boolean], e2: GR[Long]): GR[BidsRow] = GR {
    prs => import prs._
      BidsRow.tupled((<<[Int], <<[Int], <<[Int], <<[Int], <<[Boolean], <<[Long]))
  }

  /** Table description of table bids. Objects of this class serve as prototypes for rows in queries. */
  class Bids(_tableTag: Tag) extends Table[BidsRow](_tableTag, "bids") {
    def * = (id, bidderId, productId, price, auto, timestamp) <>(BidsRow.tupled, BidsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, bidderId.?, productId.?, price.?, auto.?, timestamp.?).shaped.<>({ r => import r._; _1.map(_ => BidsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column bidder_id DBType(INT UNSIGNED) */
    val bidderId: Column[Int] = column[Int]("bidder_id")
    /** Database column product_id DBType(INT UNSIGNED) */
    val productId: Column[Int] = column[Int]("product_id")
    /** Database column price DBType(INT UNSIGNED) */
    val price: Column[Int] = column[Int]("price")
    /** Database column auto DBType(BIT) */
    val auto: Column[Boolean] = column[Boolean]("auto")
    /** Database column timestamp DBType(BIGINT UNSIGNED) */
    val timestamp: Column[Long] = column[Long]("timestamp")

    /** Foreign key referencing Products (database name bids_ibfk_2) */
    lazy val productsFk = foreignKey("bids_ibfk_2", productId, Products)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name bids_ibfk_1) */
    lazy val usersFk = foreignKey("bids_ibfk_1", bidderId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table Bids */
  lazy val Bids = new TableQuery(tag => new Bids(tag))

  implicit def GetResultCaseChangesRow(implicit e0: GR[Int], e1: GR[Byte], e2: GR[Option[Int]], e3: GR[Long]): GR[CaseChangesRow] = GR {
    prs => import prs._
      CaseChangesRow.tupled((<<[Int], <<[Int], <<[Byte], <<?[Int], <<[Long]))
  }

  /** Table description of table case_changes. Objects of this class serve as prototypes for rows in queries. */
  class CaseChanges(_tableTag: Tag) extends Table[CaseChangesRow](_tableTag, "case_changes") {
    def * = (id, caseId, helpOption, refund, timestamp) <>(CaseChangesRow.tupled, CaseChangesRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, caseId.?, helpOption.?, refund, timestamp.?).shaped.<>({ r => import r._; _1.map(_ => CaseChangesRow.tupled((_1.get, _2.get, _3.get, _4, _5.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column case_id DBType(INT UNSIGNED) */
    val caseId: Column[Int] = column[Int]("case_id")
    /** Database column help_option DBType(TINYINT UNSIGNED) */
    val helpOption: Column[Byte] = column[Byte]("help_option")
    /** Database column refund DBType(INT UNSIGNED), Default(None) */
    val refund: Column[Option[Int]] = column[Option[Int]]("refund", O.Default(None))
    /** Database column timestamp DBType(BIGINT UNSIGNED) */
    val timestamp: Column[Long] = column[Long]("timestamp")

    /** Foreign key referencing Cases (database name case_changes_ibfk_1) */
    lazy val casesFk = foreignKey("case_changes_ibfk_1", caseId, Cases)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table CaseChanges */
  lazy val CaseChanges = new TableQuery(tag => new CaseChanges(tag))

  implicit def GetResultCaseMessagesRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[CaseMessagesRow] = GR {
    prs => import prs._
      CaseMessagesRow.tupled((<<[Int], <<[Int], <<[Int], <<[String], <<[Long]))
  }

  /** Table description of table case_messages. Objects of this class serve as prototypes for rows in queries. */
  class CaseMessages(_tableTag: Tag) extends Table[CaseMessagesRow](_tableTag, "case_messages") {
    def * = (id, caseId, senderId, message, timestamp) <>(CaseMessagesRow.tupled, CaseMessagesRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, caseId.?, senderId.?, message.?, timestamp.?).shaped.<>({ r => import r._; _1.map(_ => CaseMessagesRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column case_id DBType(INT UNSIGNED) */
    val caseId: Column[Int] = column[Int]("case_id")
    /** Database column sender_id DBType(INT UNSIGNED) */
    val senderId: Column[Int] = column[Int]("sender_id")
    /** Database column message DBType(TEXT), Length(65535,true) */
    val message: Column[String] = column[String]("message", O.DBType("TEXT"))
    /** Database column timestamp DBType(BIGINT UNSIGNED) */
    val timestamp: Column[Long] = column[Long]("timestamp")

    /** Foreign key referencing Cases (database name case_messages_ibfk_1) */
    lazy val casesFk = foreignKey("case_messages_ibfk_1", caseId, Cases)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name case_messages_ibfk_2) */
    lazy val usersFk = foreignKey("case_messages_ibfk_2", senderId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table CaseMessages */
  lazy val CaseMessages = new TableQuery(tag => new CaseMessages(tag))

  implicit def GetResultCasesRow(implicit e0: GR[Int], e1: GR[Byte], e2: GR[String], e3: GR[Long], e4: GR[Boolean], e5: GR[Option[Byte]], e6: GR[Option[String]], e7: GR[Option[Int]]): GR[CasesRow] = GR {
    prs => import prs._
      CasesRow.tupled((<<[Int], <<[Int], <<[Int], <<[Int], <<[Byte], <<[String], <<[Long], <<[Boolean], <<?[Byte], <<?[Byte], <<?[String], <<?[Int]))
  }

  /** Table description of table cases. Objects of this class serve as prototypes for rows in queries. */
  class Cases(_tableTag: Tag) extends Table[CasesRow](_tableTag, "cases") {
    def * = (id, sellId, openedById, openedAgainstId, problem, message, opened, closed, itemProblem, helpOption, phone, refund) <>(CasesRow.tupled, CasesRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, sellId.?, openedById.?, openedAgainstId.?, problem.?, message.?, opened.?, closed.?, itemProblem, helpOption, phone, refund).shaped.<>({ r => import r._; _1.map(_ => CasesRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9, _10, _11, _12)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column sell_id DBType(INT UNSIGNED) */
    val sellId: Column[Int] = column[Int]("sell_id")
    /** Database column opened_by_id DBType(INT UNSIGNED) */
    val openedById: Column[Int] = column[Int]("opened_by_id")
    /** Database column opened_against_id DBType(INT UNSIGNED) */
    val openedAgainstId: Column[Int] = column[Int]("opened_against_id")
    /** Database column problem DBType(TINYINT UNSIGNED) */
    val problem: Column[Byte] = column[Byte]("problem")
    /** Database column message DBType(TEXT), Length(65535,true) */
    val message: Column[String] = column[String]("message", O.DBType("TEXT"))
    /** Database column opened DBType(BIGINT UNSIGNED) */
    val opened: Column[Long] = column[Long]("opened")
    /** Database column closed DBType(BIT) */
    val closed: Column[Boolean] = column[Boolean]("closed")
    /** Database column item_problem DBType(TINYINT UNSIGNED), Default(None) */
    val itemProblem: Column[Option[Byte]] = column[Option[Byte]]("item_problem", O.Default(None))
    /** Database column help_option DBType(TINYINT UNSIGNED), Default(None) */
    val helpOption: Column[Option[Byte]] = column[Option[Byte]]("help_option", O.Default(None))
    /** Database column phone DBType(VARCHAR), Length(16,true), Default(None) */
    val phone: Column[Option[String]] = column[Option[String]]("phone", O.Length(16, varying = true), O.Default(None))
    /** Database column refund DBType(INT UNSIGNED), Default(None) */
    val refund: Column[Option[Int]] = column[Option[Int]]("refund", O.Default(None))

    /** Foreign key referencing Sells (database name cases_ibfk_1) */
    lazy val sellsFk = foreignKey("cases_ibfk_1", sellId, Sells)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name cases_ibfk_2) */
    lazy val usersFk2 = foreignKey("cases_ibfk_2", openedById, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name cases_ibfk_3) */
    lazy val usersFk3 = foreignKey("cases_ibfk_3", openedAgainstId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table Cases */
  lazy val Cases = new TableQuery(tag => new Cases(tag))

  implicit def GetResultCategoriesRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Boolean]): GR[CategoriesRow] = GR {
    prs => import prs._
      CategoriesRow.tupled((<<[Int], <<[String], <<[Boolean], <<[Int], <<[Int]))
  }

  /** Table description of table categories. Objects of this class serve as prototypes for rows in queries. */
  class Categories(_tableTag: Tag) extends Table[CategoriesRow](_tableTag, "categories") {
    def * = (id, name, leaf, parentId, ord) <>(CategoriesRow.tupled, CategoriesRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, name.?, leaf.?, parentId.?, ord.?).shaped.<>({ r => import r._; _1.map(_ => CategoriesRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name DBType(VARCHAR), Length(32,true) */
    val name: Column[String] = column[String]("name", O.Length(32, varying = true))
    /** Database column leaf DBType(BIT) */
    val leaf: Column[Boolean] = column[Boolean]("leaf")
    /** Database column parent_id DBType(INT UNSIGNED), Default(0) */
    val parentId: Column[Int] = column[Int]("parent_id", O.Default(0))
    /** Database column ord DBType(INT UNSIGNED), Default(0) */
    val ord: Column[Int] = column[Int]("ord", O.Default(0))
  }

  /** Collection-like TableQuery object for table Categories */
  lazy val Categories = new TableQuery(tag => new Categories(tag))

  implicit def GetResultCategoryFieldsRow(implicit e0: GR[Int], e1: GR[String]): GR[CategoryFieldsRow] = GR {
    prs => import prs._
      CategoryFieldsRow.tupled((<<[Int], <<[Int], <<[String]))
  }

  /** Table description of table category_fields. Objects of this class serve as prototypes for rows in queries. */
  class CategoryFields(_tableTag: Tag) extends Table[CategoryFieldsRow](_tableTag, "category_fields") {
    def * = (id, categoryId, name) <>(CategoryFieldsRow.tupled, CategoryFieldsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, categoryId.?, name.?).shaped.<>({ r => import r._; _1.map(_ => CategoryFieldsRow.tupled((_1.get, _2.get, _3.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column category_id DBType(INT UNSIGNED) */
    val categoryId: Column[Int] = column[Int]("category_id")
    /** Database column name DBType(VARCHAR), Length(16,true) */
    val name: Column[String] = column[String]("name", O.Length(16, varying = true))

    /** Foreign key referencing Categories (database name category_fields_ibfk_1) */
    lazy val categoriesFk = foreignKey("category_fields_ibfk_1", categoryId, Categories)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table CategoryFields */
  lazy val CategoryFields = new TableQuery(tag => new CategoryFields(tag))

  implicit def GetResultCategoryFieldsValuesRow(implicit e0: GR[Int], e1: GR[String]): GR[CategoryFieldsValuesRow] = GR {
    prs => import prs._
      CategoryFieldsValuesRow.tupled((<<[Int], <<[Int], <<[String]))
  }

  /** Table description of table category_fields_values. Objects of this class serve as prototypes for rows in queries. */
  class CategoryFieldsValues(_tableTag: Tag) extends Table[CategoryFieldsValuesRow](_tableTag, "category_fields_values") {
    def * = (id, categoryFieldId, name) <>(CategoryFieldsValuesRow.tupled, CategoryFieldsValuesRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, categoryFieldId.?, name.?).shaped.<>({ r => import r._; _1.map(_ => CategoryFieldsValuesRow.tupled((_1.get, _2.get, _3.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column category_field_id DBType(INT UNSIGNED) */
    val categoryFieldId: Column[Int] = column[Int]("category_field_id")
    /** Database column name DBType(VARCHAR), Length(100,true) */
    val name: Column[String] = column[String]("name", O.Length(100, varying = true))

    /** Foreign key referencing CategoryFields (database name category_fields_values_ibfk_1) */
    lazy val categoryFieldsFk = foreignKey("category_fields_values_ibfk_1", categoryFieldId, CategoryFields)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table CategoryFieldsValues */
  lazy val CategoryFieldsValues = new TableQuery(tag => new CategoryFieldsValues(tag))

  implicit def GetResultChatMessagesRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Long]]): GR[ChatMessagesRow] = GR {
    prs => import prs._
      ChatMessagesRow.tupled((<<[Int], <<[String], <<?[Long]))
  }

  /** Table description of table chat_messages. Objects of this class serve as prototypes for rows in queries. */
  class ChatMessages(_tableTag: Tag) extends Table[ChatMessagesRow](_tableTag, "chat_messages") {
    def * = (sessionId, message, timestamp) <>(ChatMessagesRow.tupled, ChatMessagesRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (sessionId.?, message.?, timestamp).shaped.<>({ r => import r._; _1.map(_ => ChatMessagesRow.tupled((_1.get, _2.get, _3)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column session_id DBType(INT UNSIGNED) */
    val sessionId: Column[Int] = column[Int]("session_id")
    /** Database column message DBType(TEXT), Length(65535,true) */
    val message: Column[String] = column[String]("message", O.DBType("TEXT"))
    /** Database column timestamp DBType(BIGINT UNSIGNED), Default(None) */
    val timestamp: Column[Option[Long]] = column[Option[Long]]("timestamp", O.Default(None))

    /** Foreign key referencing ChatSessions (database name chat_messages_ibfk_1) */
    lazy val chatSessionsFk = foreignKey("chat_messages_ibfk_1", sessionId, ChatSessions)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table ChatMessages */
  lazy val ChatMessages = new TableQuery(tag => new ChatMessages(tag))

  implicit def GetResultChatSessionsRow(implicit e0: GR[Int], e1: GR[Long]): GR[ChatSessionsRow] = GR {
    prs => import prs._
      ChatSessionsRow.tupled((<<[Int], <<[Int], <<[Long]))
  }

  /** Table description of table chat_sessions. Objects of this class serve as prototypes for rows in queries. */
  class ChatSessions(_tableTag: Tag) extends Table[ChatSessionsRow](_tableTag, "chat_sessions") {
    def * = (id, userId, created) <>(ChatSessionsRow.tupled, ChatSessionsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userId.?, created.?).shaped.<>({ r => import r._; _1.map(_ => ChatSessionsRow.tupled((_1.get, _2.get, _3.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id DBType(INT UNSIGNED) */
    val userId: Column[Int] = column[Int]("user_id")
    /** Database column created DBType(BIGINT UNSIGNED) */
    val created: Column[Long] = column[Long]("created")

    /** Foreign key referencing Users (database name chat_sessions_ibfk_1) */
    lazy val usersFk = foreignKey("chat_sessions_ibfk_1", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table ChatSessions */
  lazy val ChatSessions = new TableQuery(tag => new ChatSessions(tag))

  implicit def GetResultCreditCardsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Byte], e3: GR[Long], e4: GR[Short]): GR[CreditCardsRow] = GR {
    prs => import prs._
      CreditCardsRow.tupled((<<[Int], <<[Int], <<[String], <<[Byte], <<[Long], <<[Byte], <<[Short], <<[Short]))
  }

  /** Table description of table credit_cards. Objects of this class serve as prototypes for rows in queries. */
  class CreditCards(_tableTag: Tag) extends Table[CreditCardsRow](_tableTag, "credit_cards") {
    def * = (id, userId, holder, cardType, number, month, year, code) <>(CreditCardsRow.tupled, CreditCardsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userId.?, holder.?, cardType.?, number.?, month.?, year.?, code.?).shaped.<>({ r => import r._; _1.map(_ => CreditCardsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id DBType(INT UNSIGNED) */
    val userId: Column[Int] = column[Int]("user_id")
    /** Database column holder DBType(VARCHAR), Length(33,true) */
    val holder: Column[String] = column[String]("holder", O.Length(33, varying = true))
    /** Database column card_type DBType(TINYINT) */
    val cardType: Column[Byte] = column[Byte]("card_type")
    /** Database column number DBType(BIGINT UNSIGNED) */
    val number: Column[Long] = column[Long]("number")
    /** Database column month DBType(TINYINT) */
    val month: Column[Byte] = column[Byte]("month")
    /** Database column year DBType(SMALLINT) */
    val year: Column[Short] = column[Short]("year")
    /** Database column code DBType(SMALLINT) */
    val code: Column[Short] = column[Short]("code")

    /** Foreign key referencing Users (database name credit_cards_ibfk_1) */
    lazy val usersFk = foreignKey("credit_cards_ibfk_1", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table CreditCards */
  lazy val CreditCards = new TableQuery(tag => new CreditCards(tag))

  implicit def GetResultDisabledIpsRow(implicit e0: GR[String]): GR[DisabledIpsRow] = GR {
    prs => import prs._
      DisabledIpsRow(<<[String])
  }

  /** Table description of table disabled_ips. Objects of this class serve as prototypes for rows in queries. */
  class DisabledIps(_tableTag: Tag) extends Table[DisabledIpsRow](_tableTag, "disabled_ips") {
    def * = ip <>(DisabledIpsRow, DisabledIpsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ip.?.shaped.<>(r => r.map(_ => DisabledIpsRow(r.get)), (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column ip DBType(VARCHAR), PrimaryKey, Length(15,true) */
    val ip: Column[String] = column[String]("ip", O.PrimaryKey, O.Length(15, varying = true))
  }

  /** Collection-like TableQuery object for table DisabledIps */
  lazy val DisabledIps = new TableQuery(tag => new DisabledIps(tag))

  implicit def GetResultDonationsRow(implicit e0: GR[Int], e1: GR[Long], e2: GR[Option[String]]): GR[DonationsRow] = GR {
    prs => import prs._
      DonationsRow.tupled((<<[Int], <<[Int], <<[Int], <<[Long], <<?[String]))
  }

  /** Table description of table donations. Objects of this class serve as prototypes for rows in queries. */
  class Donations(_tableTag: Tag) extends Table[DonationsRow](_tableTag, "donations") {
    def * = (id, userId, amount, timestamp, profileId) <>(DonationsRow.tupled, DonationsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userId.?, amount.?, timestamp.?, profileId).shaped.<>({ r => import r._; _1.map(_ => DonationsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id DBType(INT UNSIGNED) */
    val userId: Column[Int] = column[Int]("user_id")
    /** Database column amount DBType(INT UNSIGNED) */
    val amount: Column[Int] = column[Int]("amount")
    /** Database column timestamp DBType(BIGINT UNSIGNED) */
    val timestamp: Column[Long] = column[Long]("timestamp")
    /** Database column profile_id DBType(VARCHAR), Length(96,true), Default(None) */
    val profileId: Column[Option[String]] = column[Option[String]]("profile_id", O.Length(96, varying = true), O.Default(None))

    /** Foreign key referencing Users (database name donations_ibfk_1) */
    lazy val usersFk = foreignKey("donations_ibfk_1", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table Donations */
  lazy val Donations = new TableQuery(tag => new Donations(tag))

  implicit def GetResultEmailConfirmationRow(implicit e0: GR[Int], e1: GR[String]): GR[EmailConfirmationRow] = GR {
    prs => import prs._
      EmailConfirmationRow.tupled((<<[Int], <<[String]))
  }

  /** Table description of table email_confirmation. Objects of this class serve as prototypes for rows in queries. */
  class EmailConfirmation(_tableTag: Tag) extends Table[EmailConfirmationRow](_tableTag, "email_confirmation") {
    def * = (userId, code) <>(EmailConfirmationRow.tupled, EmailConfirmationRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (userId.?, code.?).shaped.<>({ r => import r._; _1.map(_ => EmailConfirmationRow.tupled((_1.get, _2.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_id DBType(INT UNSIGNED), PrimaryKey */
    val userId: Column[Int] = column[Int]("user_id", O.PrimaryKey)
    /** Database column code DBType(CHAR), Length(32,false), Default(None) */
    val code: Column[String] = column[String]("code", O.Length(32, varying = false))

    /** Foreign key referencing Users (database name email_confirmation_ibfk_1) */
    lazy val usersFk = foreignKey("email_confirmation_ibfk_1", userId, Users)(r => r.id, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table EmailConfirmation */
  lazy val EmailConfirmation = new TableQuery(tag => new EmailConfirmation(tag))

  implicit def GetResultFeedbackRow(implicit e0: GR[Int], e1: GR[Byte], e2: GR[Option[String]], e3: GR[Long]): GR[FeedbackRow] = GR {
    prs => import prs._
      FeedbackRow.tupled((<<[Int], <<[Int], <<[Int], <<[Byte], <<[Byte], <<[Byte], <<[Byte], <<[Byte], <<?[String], <<[Long]))
  }

  /** Table description of table feedback. Objects of this class serve as prototypes for rows in queries. */
  class Feedback(_tableTag: Tag) extends Table[FeedbackRow](_tableTag, "feedback") {
    def * = (id, sellerId, sellId, rating, itemAsDescribed, communication, shippingTime, shippingCharges, message, timestamp) <>(FeedbackRow.tupled, FeedbackRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, sellerId.?, sellId.?, rating.?, itemAsDescribed.?, communication.?, shippingTime.?, shippingCharges.?, message, timestamp.?).shaped.<>({ r => import r._; _1.map(_ => FeedbackRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9, _10.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column seller_id DBType(INT UNSIGNED) */
    val sellerId: Column[Int] = column[Int]("seller_id")
    /** Database column sell_id DBType(INT UNSIGNED) */
    val sellId: Column[Int] = column[Int]("sell_id")
    /** Database column rating DBType(TINYINT) */
    val rating: Column[Byte] = column[Byte]("rating")
    /** Database column item_as_described DBType(TINYINT) */
    val itemAsDescribed: Column[Byte] = column[Byte]("item_as_described")
    /** Database column communication DBType(TINYINT) */
    val communication: Column[Byte] = column[Byte]("communication")
    /** Database column shipping_time DBType(TINYINT) */
    val shippingTime: Column[Byte] = column[Byte]("shipping_time")
    /** Database column shipping_charges DBType(TINYINT) */
    val shippingCharges: Column[Byte] = column[Byte]("shipping_charges")
    /** Database column message DBType(VARCHAR), Length(80,true), Default(None) */
    val message: Column[Option[String]] = column[Option[String]]("message", O.Length(80, varying = true), O.Default(None))
    /** Database column timestamp DBType(BIGINT UNSIGNED) */
    val timestamp: Column[Long] = column[Long]("timestamp")

    /** Foreign key referencing Users (database name feedback_ibfk_1) */
    lazy val usersFk = foreignKey("feedback_ibfk_1", sellerId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table Feedback */
  lazy val Feedback = new TableQuery(tag => new Feedback(tag))

  implicit def GetResultGiftCardOrdersRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long], e3: GR[Option[Long]]): GR[GiftCardOrdersRow] = GR {
    prs => import prs._
      GiftCardOrdersRow.tupled((<<[Int], <<[Int], <<[Int], <<[String], <<[String], <<[String], <<[Long], <<[String], <<?[Long]))
  }

  /** Table description of table gift_card_orders. Objects of this class serve as prototypes for rows in queries. */
  class GiftCardOrders(_tableTag: Tag) extends Table[GiftCardOrdersRow](_tableTag, "gift_card_orders") {
    def * = (id, buyerId, amount, senderName, message, recipients, timestamp, transactionId, deliveryDate) <>(GiftCardOrdersRow.tupled, GiftCardOrdersRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, buyerId.?, amount.?, senderName.?, message.?, recipients.?, timestamp.?, transactionId.?, deliveryDate).shaped.<>({ r => import r._; _1.map(_ => GiftCardOrdersRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column buyer_id DBType(INT UNSIGNED) */
    val buyerId: Column[Int] = column[Int]("buyer_id")
    /** Database column amount DBType(INT UNSIGNED) */
    val amount: Column[Int] = column[Int]("amount")
    /** Database column sender_name DBType(VARCHAR), Length(64,true) */
    val senderName: Column[String] = column[String]("sender_name", O.Length(64, varying = true))
    /** Database column message DBType(TEXT), Length(65535,true) */
    val message: Column[String] = column[String]("message", O.DBType("TEXT"))
    /** Database column recipients DBType(TEXT), Length(65535,true) */
    val recipients: Column[String] = column[String]("recipients", O.DBType("TEXT"))
    /** Database column timestamp DBType(BIGINT UNSIGNED) */
    val timestamp: Column[Long] = column[Long]("timestamp")
    /** Database column transaction_id DBType(VARCHAR), Length(32,true) */
    val transactionId: Column[String] = column[String]("transaction_id", O.Length(32, varying = true))
    /** Database column delivery_date DBType(BIGINT UNSIGNED), Default(None) */
    val deliveryDate: Column[Option[Long]] = column[Option[Long]]("delivery_date", O.Default(None))

    /** Foreign key referencing Users (database name gift_card_orders_ibfk_1) */
    lazy val usersFk = foreignKey("gift_card_orders_ibfk_1", buyerId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table GiftCardOrders */
  lazy val GiftCardOrders = new TableQuery(tag => new GiftCardOrders(tag))

  implicit def GetResultGiftCardsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Int]], e3: GR[Boolean], e4: GR[Option[Long]]): GR[GiftCardsRow] = GR {
    prs => import prs._
      GiftCardsRow.tupled((<<[Int], <<[String], <<[Int], <<?[Int], <<[String], <<[Boolean], <<?[Long]))
  }

  /** Table description of table gift_cards. Objects of this class serve as prototypes for rows in queries. */
  class GiftCards(_tableTag: Tag) extends Table[GiftCardsRow](_tableTag, "gift_cards") {
    def * = (id, code, orderId, activatorId, email, delivered, activationDate) <>(GiftCardsRow.tupled, GiftCardsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, code.?, orderId.?, activatorId, email.?, delivered.?, activationDate).shaped.<>({ r => import r._; _1.map(_ => GiftCardsRow.tupled((_1.get, _2.get, _3.get, _4, _5.get, _6.get, _7)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column code DBType(CHAR), Length(32,false) */
    val code: Column[String] = column[String]("code", O.Length(32, varying = false))
    /** Database column order_id DBType(INT UNSIGNED) */
    val orderId: Column[Int] = column[Int]("order_id")
    /** Database column activator_id DBType(INT UNSIGNED), Default(None) */
    val activatorId: Column[Option[Int]] = column[Option[Int]]("activator_id", O.Default(None))
    /** Database column email DBType(VARCHAR), Length(64,true) */
    val email: Column[String] = column[String]("email", O.Length(64, varying = true))
    /** Database column delivered DBType(BIT) */
    val delivered: Column[Boolean] = column[Boolean]("delivered")
    /** Database column activation_date DBType(BIGINT UNSIGNED), Default(None) */
    val activationDate: Column[Option[Long]] = column[Option[Long]]("activation_date", O.Default(None))

    /** Foreign key referencing GiftCardOrders (database name gift_cards_ibfk_1) */
    lazy val giftCardOrdersFk = foreignKey("gift_cards_ibfk_1", orderId, GiftCardOrders)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name gift_cards_ibfk_2) */
    lazy val usersFk = foreignKey("gift_cards_ibfk_2", activatorId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    /** Uniqueness Index over (code) (database name code) */
    val index1 = index("code", code, unique = true)
  }

  /** Collection-like TableQuery object for table GiftCards */
  lazy val GiftCards = new TableQuery(tag => new GiftCards(tag))

  implicit def GetResultMessageFoldersRow(implicit e0: GR[Int], e1: GR[String]): GR[MessageFoldersRow] = GR {
    prs => import prs._
      MessageFoldersRow.tupled((<<[Int], <<[String], <<[Int]))
  }

  /** Table description of table message_folders. Objects of this class serve as prototypes for rows in queries. */
  class MessageFolders(_tableTag: Tag) extends Table[MessageFoldersRow](_tableTag, "message_folders") {
    def * = (id, name, userId) <>(MessageFoldersRow.tupled, MessageFoldersRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, name.?, userId.?).shaped.<>({ r => import r._; _1.map(_ => MessageFoldersRow.tupled((_1.get, _2.get, _3.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name DBType(VARCHAR), Length(16,true) */
    val name: Column[String] = column[String]("name", O.Length(16, varying = true))
    /** Database column user_id DBType(INT UNSIGNED) */
    val userId: Column[Int] = column[Int]("user_id")

    /** Foreign key referencing Users (database name message_folders_ibfk_1) */
    lazy val usersFk = foreignKey("message_folders_ibfk_1", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table MessageFolders */
  lazy val MessageFolders = new TableQuery(tag => new MessageFolders(tag))

  implicit def GetResultMessagesRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[String], e3: GR[Long], e4: GR[Boolean], e5: GR[Option[String]]): GR[MessagesRow] = GR {
    prs => import prs._
      MessagesRow.tupled((<<[Int], <<?[Int], <<?[String], <<[Int], <<[String], <<[String], <<[Long], <<[Boolean], <<[Boolean], <<?[Int], <<?[String], <<[Int]))
  }

  /** Table description of table messages. Objects of this class serve as prototypes for rows in queries. */
  class Messages(_tableTag: Tag) extends Table[MessagesRow](_tableTag, "messages") {
    def * = (id, fromId, fromUserid, toId, subject, message, creationTime, flag, msgRead, itemId, itemTitle, folderId) <>(MessagesRow.tupled, MessagesRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, fromId, fromUserid, toId.?, subject.?, message.?, creationTime.?, flag.?, msgRead.?, itemId, itemTitle, folderId.?).shaped.<>({ r => import r._; _1.map(_ => MessagesRow.tupled((_1.get, _2, _3, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10, _11, _12.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column from_id DBType(INT UNSIGNED), Default(None) */
    val fromId: Column[Option[Int]] = column[Option[Int]]("from_id", O.Default(None))
    /** Database column from_userid DBType(VARCHAR), Length(16,true), Default(None) */
    val fromUserid: Column[Option[String]] = column[Option[String]]("from_userid", O.Default(None))
    /** Database column to_id DBType(INT UNSIGNED) */
    val toId: Column[Int] = column[Int]("to_id")
    /** Database column subject DBType(VARCHAR), Length(64,true) */
    val subject: Column[String] = column[String]("subject", O.Length(64, varying = true))
    /** Database column message DBType(TEXT), Length(65535,true) */
    val message: Column[String] = column[String]("message", O.DBType("TEXT"))
    /** Database column creation_time DBType(BIGINT UNSIGNED) */
    val creationTime: Column[Long] = column[Long]("creation_time")
    /** Database column flag DBType(BIT) */
    val flag: Column[Boolean] = column[Boolean]("flag")
    /** Database column msg_read DBType(BIT) */
    val msgRead: Column[Boolean] = column[Boolean]("msg_read")
    /** Database column item_id DBType(INT UNSIGNED), Default(None) */
    val itemId: Column[Option[Int]] = column[Option[Int]]("item_id", O.Default(None))
    /** Database column item_title DBType(VARCHAR), Length(80,true), Default(None) */
    val itemTitle: Column[Option[String]] = column[Option[String]]("item_title", O.Default(None))
    /** Database column folder_id DBType(INT), Default(0) */
    val folderId: Column[Int] = column[Int]("folder_id", O.Default(0))

    /** Foreign key referencing Products (database name messages_ibfk_3) */
    lazy val productsFk = foreignKey("messages_ibfk_3", itemId, Products)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name messages_ibfk_1) */
    lazy val usersFk2 = foreignKey("messages_ibfk_1", fromId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name messages_ibfk_2) */
    lazy val usersFk3 = foreignKey("messages_ibfk_2", toId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table Messages */
  lazy val Messages = new TableQuery(tag => new Messages(tag))

  implicit def GetResultOffersRow(implicit e0: GR[Int], e1: GR[Short], e2: GR[Boolean], e3: GR[Long]): GR[OffersRow] = GR {
    prs => import prs._
      OffersRow.tupled((<<[Int], <<[Int], <<[Int], <<[Int], <<[Short], <<[Boolean], <<[Boolean], <<[Long]))
  }

  /** Table description of table offers. Objects of this class serve as prototypes for rows in queries. */
  class Offers(_tableTag: Tag) extends Table[OffersRow](_tableTag, "offers") {
    def * = (id, userId, productId, price, quantity, accepted, paid, offerTime) <>(OffersRow.tupled, OffersRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userId.?, productId.?, price.?, quantity.?, accepted.?, paid.?, offerTime.?).shaped.<>({ r => import r._; _1.map(_ => OffersRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id DBType(INT UNSIGNED) */
    val userId: Column[Int] = column[Int]("user_id")
    /** Database column product_id DBType(INT UNSIGNED) */
    val productId: Column[Int] = column[Int]("product_id")
    /** Database column price DBType(INT UNSIGNED) */
    val price: Column[Int] = column[Int]("price")
    /** Database column quantity DBType(SMALLINT UNSIGNED) */
    val quantity: Column[Short] = column[Short]("quantity")
    /** Database column accepted DBType(BIT) */
    val accepted: Column[Boolean] = column[Boolean]("accepted")
    /** Database column paid DBType(BIT) */
    val paid: Column[Boolean] = column[Boolean]("paid")
    /** Database column offer_time DBType(BIGINT UNSIGNED) */
    val offerTime: Column[Long] = column[Long]("offer_time")

    /** Foreign key referencing Products (database name offers_ibfk_2) */
    lazy val productsFk = foreignKey("offers_ibfk_2", productId, Products)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name offers_ibfk_1) */
    lazy val usersFk = foreignKey("offers_ibfk_1", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table Offers */
  lazy val Offers = new TableQuery(tag => new Offers(tag))

  implicit def GetResultPasswordRecoveryRow(implicit e0: GR[Int], e1: GR[Option[String]]): GR[PasswordRecoveryRow] = GR {
    prs => import prs._
      PasswordRecoveryRow.tupled((<<[Int], <<?[String]))
  }

  /** Table description of table password_recovery. Objects of this class serve as prototypes for rows in queries. */
  class PasswordRecovery(_tableTag: Tag) extends Table[PasswordRecoveryRow](_tableTag, "password_recovery") {
    def * = (userId, code) <>(PasswordRecoveryRow.tupled, PasswordRecoveryRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (userId.?, code).shaped.<>({ r => import r._; _1.map(_ => PasswordRecoveryRow.tupled((_1.get, _2)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_id DBType(INT UNSIGNED), PrimaryKey */
    val userId: Column[Int] = column[Int]("user_id", O.PrimaryKey)
    /** Database column code DBType(CHAR), Length(32,false), Default(None) */
    val code: Column[Option[String]] = column[Option[String]]("code", O.Length(32, varying = false), O.Default(None))

    /** Foreign key referencing Users (database name password_recovery_ibfk_1) */
    lazy val usersFk = foreignKey("password_recovery_ibfk_1", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table PasswordRecovery */
  lazy val PasswordRecovery = new TableQuery(tag => new PasswordRecovery(tag))

  implicit def GetResultPaypalAccountsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Boolean]): GR[PaypalAccountsRow] = GR {
    prs => import prs._
      PaypalAccountsRow.tupled((<<[Int], <<[Int], <<[String], <<[Boolean]))
  }

  /** Table description of table paypal_accounts. Objects of this class serve as prototypes for rows in queries. */
  class PaypalAccounts(_tableTag: Tag) extends Table[PaypalAccountsRow](_tableTag, "paypal_accounts") {
    def * = (id, userId, email, primaryAcc) <>(PaypalAccountsRow.tupled, PaypalAccountsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userId.?, email.?, primaryAcc.?).shaped.<>({ r => import r._; _1.map(_ => PaypalAccountsRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id DBType(INT UNSIGNED) */
    val userId: Column[Int] = column[Int]("user_id")
    /** Database column email DBType(VARCHAR), Length(64,true) */
    val email: Column[String] = column[String]("email", O.Length(64, varying = true))
    /** Database column primary_acc DBType(BIT) */
    val primaryAcc: Column[Boolean] = column[Boolean]("primary_acc")

    /** Foreign key referencing Users (database name paypal_accounts_ibfk_1) */
    lazy val usersFk = foreignKey("paypal_accounts_ibfk_1", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table PaypalAccounts */
  lazy val PaypalAccounts = new TableQuery(tag => new PaypalAccounts(tag))

  implicit def GetResultProductFieldValuesRow(implicit e0: GR[Int], e1: GR[String]): GR[ProductFieldValuesRow] = GR {
    prs => import prs._
      ProductFieldValuesRow.tupled((<<[Int], <<[Int], <<[Int], <<[String]))
  }

  /** Table description of table product_field_values. Objects of this class serve as prototypes for rows in queries. */
  class ProductFieldValues(_tableTag: Tag) extends Table[ProductFieldValuesRow](_tableTag, "product_field_values") {
    def * = (id, fieldId, productId, value) <>(ProductFieldValuesRow.tupled, ProductFieldValuesRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, fieldId.?, productId.?, value.?).shaped.<>({ r => import r._; _1.map(_ => ProductFieldValuesRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column field_id DBType(INT UNSIGNED) */
    val fieldId: Column[Int] = column[Int]("field_id")
    /** Database column product_id DBType(INT UNSIGNED) */
    val productId: Column[Int] = column[Int]("product_id")
    /** Database column value DBType(VARCHAR), Length(32,true) */
    val value: Column[String] = column[String]("value", O.Length(32, varying = true))

    /** Foreign key referencing CategoryFields (database name product_field_values_ibfk_1) */
    lazy val categoryFieldsFk = foreignKey("product_field_values_ibfk_1", fieldId, CategoryFields)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Products (database name product_field_values_ibfk_2) */
    lazy val productsFk = foreignKey("product_field_values_ibfk_2", productId, Products)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table ProductFieldValues */
  lazy val ProductFieldValues = new TableQuery(tag => new ProductFieldValues(tag))

  implicit def GetResultProductDetailsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Byte], e3: GR[Boolean], e4: GR[Option[Int]], e5: GR[Short], e6: GR[Option[Byte]], e7: GR[Option[String]]): GR[ProductDetailsRow] = GR {
    prs => import prs._
      ProductDetailsRow.tupled((<<[String], <<?[String], <<[Short], <<[Boolean], <<[Boolean], <<[Boolean], <<[Boolean], <<[Boolean], <<[Boolean], <<?[String], <<[Byte], <<?[Int], <<[Boolean], <<?[Byte], <<?[Int], <<?[String]))
  }

  /** GetResult implicit for fetching ProductsRow objects using plain SQL queries */
  implicit def GetResultProductsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Byte], e3: GR[Boolean], e4: GR[Option[Int]], e5: GR[Short], e6: GR[Option[Byte]], e7: GR[Option[String]], e8: GR[Long]): GR[ProductsRow] = GR {
    prs => import prs._
      ProductsRow.tupled((<<[Int], <<[Int], <<[Int], <<[String], <<[String], <<[Byte], <<[String], <<[Boolean], <<[Int], <<?[Int], <<[Int], <<[String], <<[Long], <<[Byte], <<[ProductDetailsRow]))
  }

  /** Table description of table products. Objects of this class serve as prototypes for rows in queries. */
  class Products(_tableTag: Tag) extends Table[ProductsRow](_tableTag, "products") {
    def productDetails = (description, conditionDesc, quantity, credit, paypal, bitcoin, gift, bestOffer, returns, returnsDetails, domesticService, domesticCost, domesticCollect, internationalService, internationalCost, postTo) <>(ProductDetailsRow.tupled, ProductDetailsRow.unapply)

    def * = (id, sellerId, catId, title, subtitle, itemCondition, images, fixedPrice, startingPrice, nowPrice, duration, location, creationTime, state, productDetails) <>(ProductsRow.tupled, ProductsRow.unapply)

    //    /** Maps whole row to an option. Useful for outer joins. */
    //    def ? = (id.?, sellerId.?, catId.?, title.?, subtitle.?, itemCondition.?, images.?, fixedPrice.?, startingPrice.?, nowPrice.?, duration.?, location.?, creationTime.?, state.?, productDetails.?).shaped.<>({ r => import r._; _1.map(_ => ProductsRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column seller_id DBType(INT UNSIGNED) */
    val sellerId: Column[Int] = column[Int]("seller_id")
    /** Database column cat_id DBType(INT UNSIGNED) */
    val catId: Column[Int] = column[Int]("cat_id")
    /** Database column title DBType(VARCHAR), Length(80,true) */
    val title: Column[String] = column[String]("title", O.Length(80, varying = true))
    /** Database column subtitle DBType(VARCHAR), Length(80,true) */
    val subtitle: Column[String] = column[String]("subtitle", O.Length(80, varying = true))
    /** Database column item_condition DBType(TINYINT) */
    val itemCondition: Column[Byte] = column[Byte]("item_condition")
    /** Database column condition_desc DBType(TEXT), Length(65535,true) */
    val conditionDesc: Column[Option[String]] = column[Option[String]]("condition_desc", O.DBType("TEXT"))
    /** Database column images DBType(TEXT), Length(65535,true) */
    val images: Column[String] = column[String]("images", O.DBType("TEXT"))
    /** Database column description DBType(TEXT), Length(65535,true) */
    val description: Column[String] = column[String]("description", O.DBType("TEXT"))
    /** Database column fixed_price DBType(BIT) */
    val fixedPrice: Column[Boolean] = column[Boolean]("fixed_price")
    /** Database column starting_price DBType(INT UNSIGNED) */
    val startingPrice: Column[Int] = column[Int]("starting_price")
    /** Database column now_price DBType(INT UNSIGNED), Default(None) */
    val nowPrice: Column[Option[Int]] = column[Option[Int]]("now_price", O.Default(None))
    /** Database column best_offer DBType(BIT) */
    val bestOffer: Column[Boolean] = column[Boolean]("best_offer")
    /** Database column quantity DBType(SMALLINT UNSIGNED) */
    val quantity: Column[Short] = column[Short]("quantity")
    /** Database column duration DBType(INT UNSIGNED) */
    val duration: Column[Int] = column[Int]("duration")
    /** Database column credit DBType(BIT) */
    val credit: Column[Boolean] = column[Boolean]("credit")
    /** Database column paypal DBType(BIT) */
    val paypal: Column[Boolean] = column[Boolean]("paypal")
    /** Database column bitcoin DBType(BIT) */
    val bitcoin: Column[Boolean] = column[Boolean]("bitcoin")
    /** Database column gift DBType(BIT) */
    val gift: Column[Boolean] = column[Boolean]("gift")
    /** Database column returns DBType(BIT) */
    val returns: Column[Boolean] = column[Boolean]("returns")
    /** Database column returns_details DBType(TEXT), Length(65535,true) */
    val returnsDetails: Column[Option[String]] = column[Option[String]]("returns_details", O.DBType("TEXT"))
    /** Database column location DBType(VARCHAR), Length(64,true) */
    val location: Column[String] = column[String]("location", O.Length(64, varying = true))
    /** Database column domestic_service DBType(TINYINT) */
    val domesticService: Column[Byte] = column[Byte]("domestic_service")
    /** Database column domestic_cost DBType(INT UNSIGNED), Default(None) */
    val domesticCost: Column[Option[Int]] = column[Option[Int]]("domestic_cost", O.Default(None))
    /** Database column domestic_collect DBType(BIT) */
    val domesticCollect: Column[Boolean] = column[Boolean]("domestic_collect")
    /** Database column international_service DBType(TINYINT), Default(None) */
    val internationalService: Column[Option[Byte]] = column[Option[Byte]]("international_service", O.Default(None))
    /** Database column international_cost DBType(INT UNSIGNED), Default(None) */
    val internationalCost: Column[Option[Int]] = column[Option[Int]]("international_cost", O.Default(None))
    /** Database column post_to DBType(VARCHAR), Length(64,true), Default(None) */
    val postTo: Column[Option[String]] = column[Option[String]]("post_to", O.Length(64, varying = true), O.Default(None))
    /** Database column creation_time DBType(BIGINT UNSIGNED) */
    val creationTime: Column[Long] = column[Long]("creation_time")
    /** Database column state DBType(TINYINT), Default(0) */
    val state: Column[Byte] = column[Byte]("state", O.Default(0))

    /** Foreign key referencing Categories (database name products_ibfk_2) */
    lazy val categoriesFk = foreignKey("products_ibfk_2", catId :: HNil, Categories)(r => r.id :: HNil, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name products_ibfk_1) */
    lazy val usersFk = foreignKey("products_ibfk_1", sellerId :: HNil, Users)(r => r.id :: HNil, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table Products */
  lazy val Products = new TableQuery(tag => new Products(tag))

  implicit def GetResultProductViewsRow(implicit e0: GR[Int], e1: GR[Long]): GR[ProductViewsRow] = GR {
    prs => import prs._
      ProductViewsRow.tupled((<<[Int], <<[Long]))
  }

  /** Table description of table product_views. Objects of this class serve as prototypes for rows in queries. */
  class ProductViews(_tableTag: Tag) extends Table[ProductViewsRow](_tableTag, "product_views") {
    def * = (productId, timestamp) <>(ProductViewsRow.tupled, ProductViewsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (productId.?, timestamp.?).shaped.<>({ r => import r._; _1.map(_ => ProductViewsRow.tupled((_1.get, _2.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column product_id DBType(INT UNSIGNED) */
    val productId: Column[Int] = column[Int]("product_id")
    /** Database column timestamp DBType(BIGINT UNSIGNED) */
    val timestamp: Column[Long] = column[Long]("timestamp")

    /** Foreign key referencing Products (database name product_views_ibfk_1) */
    lazy val productsFk = foreignKey("product_views_ibfk_1", productId, Products)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table ProductViews */
  lazy val ProductViews = new TableQuery(tag => new ProductViews(tag))

  implicit def GetResultScheduledPaymentsRow(implicit e0: GR[Int], e1: GR[Long], e2: GR[Boolean], e3: GR[Option[String]]): GR[ScheduledPaymentsRow] = GR {
    prs => import prs._
      ScheduledPaymentsRow.tupled((<<[Int], <<[Int], <<[Int], <<[Int], <<[Long], <<[Boolean], <<?[String]))
  }

  /** Table description of table scheduled_payments. Objects of this class serve as prototypes for rows in queries. */
  class ScheduledPayments(_tableTag: Tag) extends Table[ScheduledPaymentsRow](_tableTag, "scheduled_payments") {
    def * = (id, fromId, toId, amount, paymentDate, delivered, payKey) <>(ScheduledPaymentsRow.tupled, ScheduledPaymentsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, fromId.?, toId.?, amount.?, paymentDate.?, delivered.?, payKey).shaped.<>({ r => import r._; _1.map(_ => ScheduledPaymentsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column from_id DBType(INT UNSIGNED) */
    val fromId: Column[Int] = column[Int]("from_id")
    /** Database column to_id DBType(INT UNSIGNED) */
    val toId: Column[Int] = column[Int]("to_id")
    /** Database column amount DBType(INT UNSIGNED) */
    val amount: Column[Int] = column[Int]("amount")
    /** Database column payment_date DBType(BIGINT UNSIGNED) */
    val paymentDate: Column[Long] = column[Long]("payment_date")
    /** Database column delivered DBType(BIT) */
    val delivered: Column[Boolean] = column[Boolean]("delivered")
    /** Database column pay_key DBType(VARCHAR), Length(32,true), Default(None) */
    val payKey: Column[Option[String]] = column[Option[String]]("pay_key", O.Length(32, varying = true), O.Default(None))

    /** Foreign key referencing Users (database name scheduled_payments_ibfk_1) */
    lazy val usersFk1 = foreignKey("scheduled_payments_ibfk_1", fromId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name scheduled_payments_ibfk_2) */
    lazy val usersFk2 = foreignKey("scheduled_payments_ibfk_2", toId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table ScheduledPayments */
  lazy val ScheduledPayments = new TableQuery(tag => new ScheduledPayments(tag))

  implicit def GetResultSellsRow(implicit e0: GR[Int], e1: GR[Short], e2: GR[Boolean], e3: GR[Long]): GR[SellsRow] = GR {
    prs => import prs._
      SellsRow.tupled((<<[Int], <<[Int], <<[Int], <<[Short], <<[Int], <<[Boolean], <<[Boolean], <<[Long], <<[Int]))
  }

  /** Table description of table sells. Objects of this class serve as prototypes for rows in queries. */
  class Sells(_tableTag: Tag) extends Table[SellsRow](_tableTag, "sells") {
    def * = (id, buyerId, productId, quantity, price, paid, dispatched, soldTime, sellerId) <>(SellsRow.tupled, SellsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, buyerId.?, productId.?, quantity.?, price.?, paid.?, dispatched.?, soldTime.?, sellerId.?).shaped.<>({ r => import r._; _1.map(_ => SellsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column buyer_id DBType(INT UNSIGNED) */
    val buyerId: Column[Int] = column[Int]("buyer_id")
    /** Database column product_id DBType(INT UNSIGNED) */
    val productId: Column[Int] = column[Int]("product_id")
    /** Database column quantity DBType(SMALLINT UNSIGNED) */
    val quantity: Column[Short] = column[Short]("quantity")
    /** Database column price DBType(INT UNSIGNED) */
    val price: Column[Int] = column[Int]("price")
    /** Database column paid DBType(BIT) */
    val paid: Column[Boolean] = column[Boolean]("paid")
    /** Database column dispatched DBType(BIT) */
    val dispatched: Column[Boolean] = column[Boolean]("dispatched")
    /** Database column sold_time DBType(BIGINT UNSIGNED) */
    val soldTime: Column[Long] = column[Long]("sold_time")
    /** Database column seller_id DBType(INT UNSIGNED) */
    val sellerId: Column[Int] = column[Int]("seller_id")

    /** Foreign key referencing Products (database name sells_ibfk_2) */
    lazy val productsFk = foreignKey("sells_ibfk_2", productId, Products)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name sells_ibfk_1) */
    lazy val usersFk2 = foreignKey("sells_ibfk_1", buyerId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name sells_ibfk_3) */
    lazy val usersFk3 = foreignKey("sells_ibfk_3", sellerId, Users)(r => r.id, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table Sells */
  lazy val Sells = new TableQuery(tag => new Sells(tag))

  implicit def GetResultSuggestionsRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[Byte], e3: GR[String], e4: GR[Long]): GR[SuggestionsRow] = GR {
    prs => import prs._
      SuggestionsRow.tupled((<<[Int], <<?[Int], <<[Byte], <<[String], <<[Long]))
  }

  /** Table description of table suggestions. Objects of this class serve as prototypes for rows in queries. */
  class Suggestions(_tableTag: Tag) extends Table[SuggestionsRow](_tableTag, "suggestions") {
    def * = (id, userId, topic, message, timestamp) <>(SuggestionsRow.tupled, SuggestionsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userId, topic.?, message.?, timestamp.?).shaped.<>({ r => import r._; _1.map(_ => SuggestionsRow.tupled((_1.get, _2, _3.get, _4.get, _5.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id DBType(INT UNSIGNED), Default(None) */
    val userId: Column[Option[Int]] = column[Option[Int]]("user_id", O.Default(None))
    /** Database column topic DBType(TINYINT) */
    val topic: Column[Byte] = column[Byte]("topic")
    /** Database column message DBType(TEXT), Length(65535,true) */
    val message: Column[String] = column[String]("message", O.DBType("TEXT"))
    /** Database column timestamp DBType(BIGINT UNSIGNED) */
    val timestamp: Column[Long] = column[Long]("timestamp")

    /** Foreign key referencing Users (database name user_id) */
    lazy val usersFk = foreignKey("user_id", userId, Users)(r => r.id, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table Suggestions */
  lazy val Suggestions = new TableQuery(tag => new Suggestions(tag))

  implicit def GetResultTellUsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[TellUsRow] = GR {
    prs => import prs._
      TellUsRow.tupled((<<[Int], <<[String], <<[Long], <<[String]))
  }

  /** Table description of table tell_us. Objects of this class serve as prototypes for rows in queries. */
  class TellUs(_tableTag: Tag) extends Table[TellUsRow](_tableTag, "tell_us") {
    def * = (id, message, timestamp, ip) <>(TellUsRow.tupled, TellUsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, message.?, timestamp.?, ip.?).shaped.<>({ r => import r._; _1.map(_ => TellUsRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column message DBType(TEXT), Length(65535,true) */
    val message: Column[String] = column[String]("message", O.DBType("TEXT"))
    /** Database column timestamp DBType(BIGINT UNSIGNED) */
    val timestamp: Column[Long] = column[Long]("timestamp")
    /** Database column ip DBType(VARCHAR), Length(15,true) */
    val ip: Column[String] = column[String]("ip", O.Length(15, varying = true))
  }

  /** Collection-like TableQuery object for table TellUs */
  lazy val TellUs = new TableQuery(tag => new TellUs(tag))

  implicit def GetResultTrackingRow(implicit e0: GR[Int], e1: GR[String]): GR[TrackingRow] = GR {
    prs => import prs._
      TrackingRow.tupled((<<[Int], <<[Int], <<[String], <<[String]))
  }

  /** Table description of table tracking. Objects of this class serve as prototypes for rows in queries. */
  class Tracking(_tableTag: Tag) extends Table[TrackingRow](_tableTag, "tracking") {
    def * = (id, sellId, number, courier) <>(TrackingRow.tupled, TrackingRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, sellId.?, number.?, courier.?).shaped.<>({ r => import r._; _1.map(_ => TrackingRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column sell_id DBType(INT UNSIGNED) */
    val sellId: Column[Int] = column[Int]("sell_id")
    /** Database column number DBType(VARCHAR), Length(20,true) */
    val number: Column[String] = column[String]("number", O.Length(20, varying = true))
    /** Database column courier DBType(VARCHAR), Length(20,true) */
    val courier: Column[String] = column[String]("courier", O.Length(20, varying = true))

    /** Foreign key referencing Sells (database name tracking_ibfk_1) */
    lazy val sellsFk = foreignKey("tracking_ibfk_1", sellId, Sells)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
  }

  /** Collection-like TableQuery object for table Tracking */
  lazy val Tracking = new TableQuery(tag => new Tracking(tag))

  implicit def GetResultUsersRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long], e3: GR[Boolean], e4: GR[Option[Long]], e5: GR[Option[String]], e6: GR[Option[Int]]): GR[UsersRow] = GR {
    prs => import prs._
      UsersRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[String], <<[String], <<[Long], <<[Boolean], <<[Boolean], <<[Boolean], <<?[Long], <<[Int], <<?[String], <<?[String], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[Int], <<?[String], <<?[String]))
  }

  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends Table[UsersRow](_tableTag, "users") {
    def * = (id, userid, password, email, name, lname, registrationDate, emailConfirmed, suspended, deleted, deletedDate, giftCardBalance, question, answer, registrationAddressId, fromAddressId, toAddressId, returnAddressId, paymentAddressId, phone, accountType) <>(UsersRow.tupled, UsersRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userid.?, password.?, email.?, name.?, lname.?, registrationDate.?, emailConfirmed.?, suspended.?, deleted.?, deletedDate, giftCardBalance.?, question, answer, registrationAddressId, fromAddressId, toAddressId, returnAddressId, paymentAddressId, phone, accountType).shaped.<>({ r => import r._; _1.map(_ => UsersRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11, _12.get, _13, _14, _15, _16, _17, _18, _19, _20, _21)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column userid DBType(VARCHAR), Length(16,true) */
    val userid: Column[String] = column[String]("userid", O.Length(16, varying = true))
    /** Database column password DBType(CHAR), Length(60,false) */
    val password: Column[String] = column[String]("password", O.Length(60, varying = false))
    /** Database column email DBType(VARCHAR), Length(64,true) */
    val email: Column[String] = column[String]("email", O.Length(64, varying = true))
    /** Database column name DBType(VARCHAR), Length(16,true) */
    val name: Column[String] = column[String]("name", O.Length(16, varying = true))
    /** Database column lname DBType(VARCHAR), Length(16,true) */
    val lname: Column[String] = column[String]("lname", O.Length(16, varying = true))
    /** Database column registration_date DBType(BIGINT UNSIGNED) */
    val registrationDate: Column[Long] = column[Long]("registration_date")
    /** Database column email_confirmed DBType(BIT) */
    val emailConfirmed: Column[Boolean] = column[Boolean]("email_confirmed")
    /** Database column suspended DBType(BIT) */
    val suspended: Column[Boolean] = column[Boolean]("suspended")
    /** Database column deleted DBType(BIT) */
    val deleted: Column[Boolean] = column[Boolean]("deleted")
    /** Database column deleted_date DBType(BIGINT UNSIGNED), Default(None) */
    val deletedDate: Column[Option[Long]] = column[Option[Long]]("deleted_date", O.Default(None))
    /** Database column gift_card_balance DBType(INT UNSIGNED), Default(0) */
    val giftCardBalance: Column[Int] = column[Int]("gift_card_balance", O.Default(0))
    /** Database column question DBType(VARCHAR), Length(64,true), Default(None) */
    val question: Column[Option[String]] = column[Option[String]]("question", O.Length(64, varying = true), O.Default(None))
    /** Database column answer DBType(VARCHAR), Length(16,true), Default(None) */
    val answer: Column[Option[String]] = column[Option[String]]("answer", O.Length(16, varying = true), O.Default(None))
    /** Database column registration_address_id DBType(INT UNSIGNED), Default(None) */
    val registrationAddressId: Column[Option[Int]] = column[Option[Int]]("registration_address_id", O.Default(None))
    /** Database column from_address_id DBType(INT UNSIGNED), Default(None) */
    val fromAddressId: Column[Option[Int]] = column[Option[Int]]("from_address_id", O.Default(None))
    /** Database column to_address_id DBType(INT UNSIGNED), Default(None) */
    val toAddressId: Column[Option[Int]] = column[Option[Int]]("to_address_id", O.Default(None))
    /** Database column return_address_id DBType(INT UNSIGNED), Default(None) */
    val returnAddressId: Column[Option[Int]] = column[Option[Int]]("return_address_id", O.Default(None))
    /** Database column payment_address_id DBType(INT UNSIGNED), Default(None) */
    val paymentAddressId: Column[Option[Int]] = column[Option[Int]]("payment_address_id", O.Default(None))

    val phone: Column[Option[String]] = column[Option[String]]("phone", O.Default(None))
    val accountType: Column[Option[String]] = column[Option[String]]("account_type", O.Default(None))

    /** Uniqueness Index over (email) (database name email) */
    val index1 = index("email", email, unique = true)
    /** Uniqueness Index over (userid) (database name userid) */
    val index2 = index("userid", userid, unique = true)
  }

  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))

  implicit def GetResultWatchlistRow(implicit e0: GR[Int]): GR[WatchlistRow] = GR {
    prs => import prs._
      WatchlistRow.tupled((<<[Int], <<[Int], <<[Int]))
  }

  /** Table description of table watchlist. Objects of this class serve as prototypes for rows in queries. */
  class Watchlist(_tableTag: Tag) extends Table[WatchlistRow](_tableTag, "watchlist") {
    def * = (id, userId, productId) <>(WatchlistRow.tupled, WatchlistRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userId.?, productId.?).shaped.<>({ r => import r._; _1.map(_ => WatchlistRow.tupled((_1.get, _2.get, _3.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id DBType(INT UNSIGNED) */
    val userId: Column[Int] = column[Int]("user_id")
    /** Database column product_id DBType(INT UNSIGNED) */
    val productId: Column[Int] = column[Int]("product_id")

    /** Foreign key referencing Products (database name watchlist_ibfk_2) */
    lazy val productsFk = foreignKey("watchlist_ibfk_2", productId, Products)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name watchlist_ibfk_1) */
    lazy val usersFk = foreignKey("watchlist_ibfk_1", userId, Users)(r => r.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    /** Uniqueness Index over (userId,productId) (database name user_id) */
    val index1 = index("user_id", (userId, productId), unique = true)
  }

  /** Collection-like TableQuery object for table Watchlist */
  lazy val Watchlist = new TableQuery(tag => new Watchlist(tag))

  /** GetResult implicit for fetching SessionsRow objects using plain SQL queries */
  implicit def GetResultSessionsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[SessionsRow] = GR {
    prs => import prs._
      SessionsRow.tupled((<<[Int], <<[String], <<[String], <<[Long]))
  }

  /** Table description of table sessions. Objects of this class serve as prototypes for rows in queries. */
  class Sessions(_tableTag: Tag) extends Table[SessionsRow](_tableTag, "sessions") {
    def * = (id, userid, hash, creationTime) <>(SessionsRow.tupled, SessionsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userid.?, hash.?, creationTime.?).shaped.<>({ r => import r._; _1.map(_ => SessionsRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id DBType(INT UNSIGNED) */
    val userid: Column[String] = column[String]("userid")
    /** Database column hash DBType(VARCHAR), Length(32,true) */
    val hash: Column[String] = column[String]("hash", O.Length(32, varying = true))
    /** Database column creation_time DBType(BIGINT) */
    val creationTime: Column[Long] = column[Long]("creation_time")

    /** Foreign key referencing Users (database name sessions_ibfk_1) */
    lazy val usersFk = foreignKey("sessions_ibfk_1", userid, Users)(r => r.userid, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    /** Index over (hash) (database name hash) */
    val index1 = index("hash", hash, unique = true)

  }

  /** Collection-like TableQuery object for table Sessions */
  lazy val Sessions = new TableQuery(tag => new Sessions(tag))


  /** GetResult implicit for fetching TokensRow objects using plain SQL queries */
  implicit def GetResultTokensRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[TokensRow] = GR {
    prs => import prs._
      TokensRow.tupled((<<[Int], <<[String], <<[String], <<[Long]))
  }

  /** Table description of table tokens. Objects of this class serve as prototypes for rows in queries. */
  class Tokens(_tableTag: Tag) extends Table[TokensRow](_tableTag, "tokens") {
    def * = (id, userid, hash, creationTime) <>(TokensRow.tupled, TokensRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, userid.?, hash.?, creationTime.?).shaped.<>({ r => import r._; _1.map(_ => TokensRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id DBType(INT UNSIGNED), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column userid DBType(VARCHAR), Length(16,true) */
    val userid: Column[String] = column[String]("userid", O.Length(16, varying = true))
    /** Database column hash DBType(VARCHAR), Length(64,true) */
    val hash: Column[String] = column[String]("hash", O.Length(64, varying = true))
    /** Database column creation_time DBType(BIGINT) */
    val creationTime: Column[Long] = column[Long]("creation_time")

    /** Foreign key referencing Users (database name tokens_ibfk_1) */
    lazy val usersFk = foreignKey("tokens_ibfk_1", userid, Users)(r => r.userid, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)

    /** Uniqueness Index over (hash) (database name hash) */
    val index1 = index("hash", hash, unique = true)
  }

  /** Collection-like TableQuery object for table Tokens */
  lazy val Tokens = new TableQuery(tag => new Tokens(tag))

  implicit def GetResultPurchaseRow(implicit e0: GR[Int], e1: GR[String], e4: GR[Option[Int]], e7: GR[Option[String]]): GR[PurchaseRow] = GR {
    prs => import prs._
      PurchaseRow.tupled((<<[SellsRow], <<?[String], <<[String], <<[String], <<[String], <<[String], <<?[Int], <<?[Int], <<[Int], <<[String], <<[Int], <<[Int], <<?[Int]))
  }

    /** Table description of table suc_confirmation. Objects of this class serve as prototypes for rows in queries. */
  // class SUCConfirmation(_tableTag: Tag) extends Table[SUCConfirmationRow](_tableTag, "email_confirmation") {
  //   def * = (userId, code) <>(SUCConfirmationRow.tupled, SUCConfirmationRow.unapply)

  //   /** Maps whole row to an option. Useful for outer joins. */
  //   def ? = (userId.?, code.?).shaped.<>({ r => import r._; _1.map(_ => SUCConfirmationRow.tupled((_1.get, _2.get)))}, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

  //   /** Database column user_id DBType(INT UNSIGNED), PrimaryKey */
  //   val userId: Column[Int] = column[Int]("user_id", O.PrimaryKey)
  //   /** Database column code DBType(CHAR), Length(32,false), Default(None) */
  //   val code: Column[String] = column[String]("code", O.Length(32, varying = false))

  //   /** Foreign key referencing Users (database name suc_confirmation_ibfk_1) */
  //   lazy val usersFk = foreignKey("suc_confirmation_ibfk_1", userId, Users)(r => r.id, onDelete = ForeignKeyAction.Cascade)
  // }

  
}
