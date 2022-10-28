package com.nichoshop.legacy.dao.slick

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.legacy.dao.TrackingDAO
import com.nichoshop.legacy.models.TrackingRow
import com.nichoshop.legacy.slick.Tables._

class SlickTrackingDAO(val db: Database) extends TrackingDAO {
  def create(t: TrackingRow) = db.withSession { implicit session =>
    Tracking += t
  }
}
