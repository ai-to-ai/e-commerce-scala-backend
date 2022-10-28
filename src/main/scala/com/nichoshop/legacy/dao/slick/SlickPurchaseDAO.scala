package com.nichoshop.legacy.dao.slick

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.legacy.dao.PurchaseDAO
import com.nichoshop.legacy.models.PurchaseRow
import com.nichoshop.legacy.slick.Tables._

import scala.slick.jdbc.StaticQuery.interpolation

class SlickPurchaseDAO(val db: Database) extends PurchaseDAO {

  def findAll(userId: Int, from: Int, size: Int): List[PurchaseRow] = db.withSession { implicit session =>
    sql"""SELECT
      s.*,
      t.number                    AS trackingNumber,
      a1.country                  AS toAddress,
      a2.country                  AS fromAddress,
      p.title,
      p.images,
      p.international_cost,
      p.domestic_cost,
      u1.id                       AS sellerId,
      u1.name                     AS sellerName,
      sum(if(f.rating > 0, 1, 0)) AS positiveFeeds,
      sum(if(f.rating < 0, 1, 0)) AS negativeFeeds,
      f1.id                       AS feedback_id
      FROM sells s JOIN products p ON p.id = s.product_id
      JOIN users u1 ON u1.id = p.seller_id
      JOIN users u2 ON u2.id = s.buyer_id
      LEFT JOIN feedback f ON f.seller_id = p.seller_id
      JOIN addresses a1 ON a1.id = u2.to_address_id
      JOIN addresses a2 ON a2.id = u1.from_address_id
      LEFT JOIN feedback f1 ON f1.sell_id = s.id
      LEFT JOIN tracking t ON t.sell_id = s.id
      WHERE u2.id = $userId ORDER BY s.sold_time LIMIT $from, $size""".as[PurchaseRow].list
  }
}
