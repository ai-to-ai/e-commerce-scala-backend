package com.nichoshop

import com.nichoshop.utils.DS
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import java.util.Properties
import scala.slick.driver.JdbcDriver


/**
 * Created by Evgeny Zhoga on 01.06.15.
 */
object Environment {
  private val log = LoggerFactory.getLogger(getClass)

  val config = ConfigFactory.load("application.conf")
  val isProduction : Boolean = config.getString("environment.type") == "production"

  val skipAuth : Boolean = config.getBoolean("skipAuth")

  val adminId : String = 
      if(isProduction) config.getString("adminId.stable")
      else config.getString("adminId.test")

  val driver: JdbcDriver =
      if (config.getString("db.type") == "mysql")scala.slick.driver.MySQLDriver
      else scala.slick.driver.HsqldbDriver

  val db = {
    import driver.simple._

    import scala.collection.JavaConversions._
    val p = new Properties {
      if(isProduction)   config.getConfig(s"${config.getString("db.type")}.db.stable").
        entrySet().
        foreach ( entry => setProperty(entry.getKey, entry.getValue.unwrapped().toString) )
      else  config.getConfig(s"${config.getString("db.type")}.db.test").
        entrySet().
        foreach ( entry => setProperty(entry.getKey, entry.getValue.unwrapped().toString) )
    }
    Database.forDataSource(DS.create(p))

  }

  object recaptcha {
    val uri : String = if (isProduction) config.getString("recaptcha.stable.uri")
      else config.getString("recaptcha.test.uri")

    val secret : String = if (isProduction) config.getString("recaptcha.stable.secret")
      else config.getString("recaptcha.test.secret")
  }

  val skipCaptcha : Boolean = 
    if(isProduction) config.getBoolean("recaptcha.stable.skip")
    else config.getBoolean("recaptcha.test.skip")

  val host : String = config.getString("nichoshop.host")
  val apiHost : String = config.getString("nichoshop.api-host")
  val hostWithRootPath : String = config.getString("nichoshop.protocol") + "://" + apiHost + config.getString("nichoshop.root-path")

  object twilio {
    val accountSid : String =
      if (isProduction) config.getString("twilio.stable.accountSid")
      else config.getString("twilio.test.accountSid")

    val authToken : String =
      if (isProduction) config.getString("twilio.stable.authToken")
      else config.getString("twilio.test.authToken")

    val from : String =
      if (isProduction) config.getString("twilio.stable.from")
      else config.getString("twilio.test.from")
  }

  object duo {
    val clientId: String = 
      if(isProduction) config.getString("duo.stable.client-id")
      else config.getString("duo.test.client-id")
    val clientSecret: String  = 
      if(isProduction) config.getString("duo.stable.client-secret")
      else config.getString("duo.test.client-secret")
    val apiHost: String  = 
      if(isProduction) config.getString("duo.stable.api-host")
      else config.getString("duo.test.api-host")
    val redirectUri: String  = 
      if(isProduction) config.getString("duo.stable.redirect-uri")
      else config.getString("duo.test.redirect-uri")
    val redirectSUCUri: String  = 
      if(isProduction) config.getString("duo.stable.redirect-suc-uri")
      else config.getString("duo.test.redirect-suc-uri")
  }



}
