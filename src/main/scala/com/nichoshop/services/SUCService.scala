package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.model.dto._
import com.nichoshop.models.SUCs
import com.nichoshop.models.helpers.DB
import com.nichoshop.utils.Converters._
import com.nichoshop.models._
import org.mindrot.jbcrypt.BCrypt

/*
 *  Created by Nursultan on 5/12/2022
 */

class SUCService{

    val sucs = SUCs.query

    def getSUCsByUserId(userId: Int) : List[String] = DB.read { implicit session =>
        sucs.filter(_.userId === userId)
            .list.map(_.code)
    }

    def findLastOne(userId: Int, sucType: Int): Option[SUCEntity] = DB.read { implicit s =>
        sucs.filter(_.userId === userId)
            .filter(_.sucType === sucType)
            .sortBy(_.created.desc)
            .run.headOption
  }
    def createSUC(userId: Int, code: String, sucType: Int) = DB.write { implicit s =>
        SUCs.insert(new SUCEntity(userId = userId, code = code, sucType = sucType))    
    }
    def deleteById(id: Int) =  DB.write { implicit s =>
        SUCs.delete(id)
    }
}