package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._
import com.nichoshop.utils.{SecureUtils, TokenIsCompromisedException}

import scala.slick.lifted.Tag

/**
 * Created by Evgeny Zhoga on 15.06.15.
 */
case class TokenEntity(

            override val id:Option[Int] = None,
            userid: String,
            hash: String,
            creationTime: Long  = System.currentTimeMillis(),
            hashSession: String
            
) extends IdAsPrimaryKey

class Tokens(tag:Tag) extends TableWithIdAsPrimaryKey[TokenEntity](tag, "tokens") {
  def userid = column[String]("userid")
  def hash = column[String]("hash")
  def creationTime = column[Long]("creation_time")
  def hashSession = column[String]("hash_session")

  def uniqueHashIndex = index("unique_hash", hash, unique = true)
  def hashSessionIndex = index("hash_session", userid, unique = true)

  def * = (id.?, userid, hash, creationTime, hashSession) <> (TokenEntity.tupled, TokenEntity.unapply)
}

object Tokens extends QueryForTableWithIdAsPrimaryKey[TokenEntity, Tokens](TableQuery[Tokens]) {
  def findByHash(hash: String): Option[TokenEntity] = DB.read ({ implicit s =>
    query.filter(_.hash === hash).firstOption
  })
  def findByHashSession(hashSession: String): Option[TokenEntity] = DB.read ({ implicit s =>
    query.filter(_.hashSession === hashSession).firstOption
  })
  def newToken(userid: String): TokenEntity = DB.write ({ implicit s =>
    val token = TokenEntity(
      userid = userid,
      hash = SecureUtils.generateSHAToken("hash_"),
      hashSession = SecureUtils.generateSHAToken("hash_session_")
    )
    token.copy(id = Some(insert(token)))
  })

  /**
   * Return token with updated hash value in case when hashSession is found and current hash is
   * the same as on got from user
   * Otherwise throws TokenIsCompromisedException and remove all user token sessions
   *
   * @param hash got from user
   * @param hashSession got from user
   * @return return token with updated hash value if token with hash and hashSession is found
   */
  def nextToken(hash: String, hashSession: String): Option[TokenEntity] = {
    findByHashSession(hashSession).map {existingToken =>
      if (hash == existingToken.hash) {
        DB.write({implicit s =>
          val token = existingToken.copy(hash = SecureUtils.generateSHAToken("hash_"))
          update(token)
          token
        })
      } else {
        DB.write({implicit s => query.filter(_.userid === existingToken.userid).delete})
        throw new TokenIsCompromisedException
      }
    }
  }
}