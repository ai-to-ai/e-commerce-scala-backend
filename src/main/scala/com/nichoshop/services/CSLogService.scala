package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers.DB
import com.nichoshop.models.{CSLogEntity, CSLogs}
import com.nichoshop.utils.Pagination

import org.slf4j
import org.slf4j.LoggerFactory

class CSLogService {

    val csLogs = CSLogs.query

    // def login(csId: Int) = DB.write { implicit session =>
    //     CSLogs.insert()
    // }

}