package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.model.dto._
import com.nichoshop.models.helpers._
import com.nichoshop.models.{AdminNotes, AdminNoteEntity}

import scala.collection.JavaConversions._

/**
 * Created by Nursultan on 7/8/2022
 */
class AdminNoteService {

    val adminNotes = AdminNotes.query

    def getList(csId : Int) = DB.read { implicit session =>
        adminNotes.filter(_.csId === csId).list
    }

    def create(x: AdminNoteEntity) = DB.write { implicit session =>
        AdminNotes.insert(x)    
    }

    def update(x: AdminNoteEntity) = DB.write { implicit session =>
        AdminNotes.update(x)    
    }
    
    def delete(id: Int) = DB.write { implicit session =>
        AdminNotes.delete(id)
    }

}