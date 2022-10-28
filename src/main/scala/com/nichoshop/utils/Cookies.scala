package com.nichoshop.utils


import com.nichoshop.models.Tokens
import org.slf4j.LoggerFactory

import java.util.concurrent.TimeUnit
import javax.servlet.http.{Cookie, HttpServletRequest, HttpServletResponse}

/**
 * Created by Evgeny Zhoga on 06.06.15.
 */
object Cookies {
  val REMEMBER_ME = "RememberMe_Nichoshop"
  private val log = LoggerFactory.getLogger(getClass)

  object rememberMe {
    import javax.servlet.http.HttpServletRequest

    def getAge(implicit request: HttpServletRequest) = {

    }
  }

  def getTokenCookie(implicit request: HttpServletRequest, response: HttpServletResponse): Option[(String, String)] = {
    request.getCookies.find(_.getName == REMEMBER_ME).map(_.getValue).filter(_.contains(".")).map(_.split(".")).
      flatMap {
        case Array(hashSession, hash) =>
          Some(hashSession -> hash)
        case _ => None
    }
  }
  def newTokenCookie(userid: String)(implicit request: HttpServletRequest, response: HttpServletResponse) = {
    request.getCookies.find(_.getName == REMEMBER_ME) match {
      case None =>
        val token = Tokens.newToken(userid)
        response.addCookie(new Cookie(REMEMBER_ME, s"${token.hashSession}.${token.hash}") {
          setPath("/")
          setMaxAge(TimeUnit.DAYS.toSeconds(7).toInt)
        })
      case _ =>
    }
  }
  def withTokenCookie[T](body: (String) => Option[T])(implicit request: HttpServletRequest, response: HttpServletResponse): Option[T] = {
    Option(request.getCookies).map(_.toList).getOrElse(List()).
      find(_.getName == REMEMBER_ME).
      map(_.getValue).
      filter(_.contains(".")).
      map(_.split(".")).
    flatMap {
      case Array(hashSession, hash) =>
        Tokens.nextToken(hash, hashSession).flatMap { token =>
          response.addCookie(new Cookie(REMEMBER_ME, s"${token.hashSession}.${token.hash}") {
            setPath("/")
            setMaxAge(TimeUnit.DAYS.toSeconds(7).toInt)
          })
          body(hashSession)
        }
      case _ => None
    }
  }

  def checkCookie(getCookie: String => Option[String], setCookie: (String, String) => Unit): Option[String] = {
    getCookie(REMEMBER_ME).filter(_.contains(".")).map(s => {
      log.info(s" >>>>> cookie: $s")
      val v = s.split('.')
      v(0) -> v(1)
    }).flatMap {t =>
      Tokens.nextToken(t._2, t._1).map {token =>
        setCookie(REMEMBER_ME, s"${token.hashSession}.${token.hash}")
        t._1
      }
    }


  }
}
