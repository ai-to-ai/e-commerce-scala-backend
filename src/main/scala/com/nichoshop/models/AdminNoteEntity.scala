package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 6/30/2022
 */

case class AdminNoteEntity(

            override val id: Option[Int] = None,
            subject: String,
            desc: String,
            csId: Int,
            created: Long = System.currentTimeMillis()

) extends IdAsPrimaryKey

class AdminNotes(tag:Tag) extends TableWithIdAsPrimaryKey[AdminNoteEntity](tag, "admin_notes") {

  def subject = column[String]("subject")
  def desc = column[String]("desc")
  def csId = column[Int]("cs_id")
  def created = column[Long]("created")

  def * = (id.?, subject, desc, csId, created) <> (AdminNoteEntity.tupled, AdminNoteEntity.unapply)
}

object AdminNotes extends QueryForTableWithIdAsPrimaryKey[AdminNoteEntity, AdminNotes](TableQuery[AdminNotes]) {
}