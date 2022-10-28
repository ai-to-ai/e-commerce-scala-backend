package com.nichoshop.legacy.dao.slick

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.legacy.dao.SellDAO
import com.nichoshop.legacy.models.SellsRow
import com.nichoshop.legacy.slick.Tables._

class SlickSellDAO(val db: Database) extends SellDAO {
  def findById(id: Int): Option[SellsRow] = db.withSession { implicit session =>
    Sells.filter(_.id === id).take(1).list.headOption
  }

  def markAsDispatched(sellerId: Int, sellId: Int) = db.withSession { implicit session =>
    Sells.filter(s => s.id === sellId && s.sellerId === sellerId).map(_.dispatched).update(true)
  }
}