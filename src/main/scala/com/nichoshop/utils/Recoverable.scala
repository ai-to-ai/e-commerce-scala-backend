package com.nichoshop.utils

import org.json4s.JsonAST.{JObject, JString}
import org.json4s.jackson.JsonMethods._
import org.scalatra.Control

import scala.language.implicitConversions
import scala.util.control.NonFatal

trait Recoverable extends Control with Logger {

  implicit def recoverable(action: => Any): Any = try {
    action
  } catch {
    case NonFatal(e) =>
      log.error("exception happens: ", e)
      halt(400, compact(JObject(List("error" -> JString(e.getMessage)))))
  }
}
