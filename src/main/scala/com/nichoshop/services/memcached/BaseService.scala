package com.nichoshop.services.memcached

import com.nichoshop.model.dto._
import com.nichoshop.utils.Memcached
import com.nichoshop.utils.Memcached._
import monix.execution.CancelableFuture
import org.slf4j.LoggerFactory
import shade.memcached.Codec

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import net.spy.memcached.MemcachedClient
import java.net.InetSocketAddress

import scala.concurrent.duration._
/**
 * Created by Evgeny Zhoga on 06.09.15.
 */
trait BaseService {
  val log = LoggerFactory.getLogger(getClass)
  val mcc : MemcachedClient = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211))


  protected def set(key: String, value: AnyRef) = {
    log.info(s"MEMCACHED[SET]: $key")


    value match {
      case v: UserDto => Memcached.client.set(key, v, 1 day).onComplete(b=> println(b))
      case v: SessionDto => Memcached.client.set(key, v, 1 day).onComplete(b=> println(b))
      case v: CartDto => Memcached.client.set(key, v, 1 day).onComplete(b=> println(b))
      case v: String => Memcached.client.set(key, v, 1 day).onComplete(b=> println(b))
    }
  }

  protected def get[T](key: String)(implicit codec: Codec[T]): Option[T] = {
    log.info(s"MEMCACHED[GET]: $key")
    Memcached.client.awaitGet[T](key)
  }

  protected def delete(key: String): CancelableFuture[Boolean] = {
    log.info(s"MEMCACHED[DELETE]: $key")
    Memcached.client.delete(key)
  }

}
