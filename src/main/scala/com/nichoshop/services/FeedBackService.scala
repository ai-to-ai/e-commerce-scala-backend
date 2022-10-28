package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers.DB
import com.nichoshop.models.{FeedBackEntity, FeedBacks}
import com.nichoshop.utils.Pagination

import org.slf4j
import org.slf4j.LoggerFactory

class FeedBackService {

    val feedBacks = FeedBacks.query

    def create(x: FeedBackEntity) = DB.write { implicit session =>
        feedBacks.insert(x)
    }

    def getList(userId: Int) = DB.read { implicit session =>
        feedBacks.filter(_.userId === userId).list
    }






}