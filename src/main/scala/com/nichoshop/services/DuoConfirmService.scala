package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers.DB
import com.nichoshop.models.{DuoConfirmEntity, DuoConfirms, Users, UserEntity}

import org.slf4j
import org.slf4j.LoggerFactory

class DuoConfirmService {

    val duoConfirms = DuoConfirms.query
    val users = Users.query
    
    def create(x: DuoConfirmEntity) = DB.write { implicit session =>
        duoConfirms.insert(x)
    }

    def getStateByUserName(userName: String) = DB.read { implicit session =>
        duoConfirms.filter(_.userName === userName)
                    .map(_.state)
                    .run.headOption
    }
    def getUserNameByState(state: String) = DB.read { implicit session =>
        duoConfirms.filter(_.state === state)
                    .map(_.userName)
                    .run.headOption    
    }

    def getUserByState(state: String) = DB.read { implicit session =>
        users.filter(u => u.userid in duoConfirms.filter(_.state === state).map(_.userName))
                .run.headOption
    }
    
    def deleteByUserId(userName: String) = DB.write { implicit session =>
        duoConfirms.filter(_.userName === userName).delete    
    }

    def deleteByState(state: String) = DB.write { implicit session =>
        duoConfirms.filter(_.state === state).delete    
    }


}