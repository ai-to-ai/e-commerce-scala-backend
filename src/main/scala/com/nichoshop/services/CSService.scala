package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers.DB
import com.nichoshop.models.{CSEntity, CSs}
import com.nichoshop.utils.Pagination

import org.slf4j
import org.slf4j.LoggerFactory

class CSService {

    val cs = CSs.query

    def create(x: CSEntity) = DB.write { implicit session =>
        CSs.insert(x)
    }

    def getList() = DB.read { implicit session =>
        cs.list
    }

    private def getCustomerSupportUsername(supreme: Boolean) = DB.read { implicit session => 
        val usernameList = cs.map(_.username).list
        val supremeList = usernameList.filter(name => name.indexOf("#") > 0)
        val departList = usernameList.filter(name => name.indexOf("_") > 0)
        
        // supremeList.map(name => )
    
    }

}