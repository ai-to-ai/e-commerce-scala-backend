package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers.DB
import com.nichoshop.models.{ClaimEntity, Claims, Users, UserEntity}

import org.slf4j
import org.slf4j.LoggerFactory

class ClaimService {

    val claims = Claims.query
    val users = Users.query


}