package com.nichoshop.utils


import com.nichoshop.model.dto._
import org.apache.avro.io.{DecoderFactory, EncoderFactory}
import org.apache.avro.file.SeekableByteArrayInput
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}
import shade.memcached.{Memcached => SMemcached, _}

import java.io.{ByteArrayOutputStream, ByteArrayInputStream}
import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.duration._

/**
 * Created by Evgeny Zhoga on 03.06.15.
 */
object Memcached {
//  val client = SMemcached(Configuration("127.0.0.1:11211", authentication = Option(AuthConfiguration(username = "nichoshop", password = "YjBlYzc1NmE2OGQxODViNTgwYzE1Nzcz"))))
  val client = SMemcached(Configuration("127.0.0.1:11211", operationTimeout=5.second))

  implicit object ByteArrayBinaryCodec extends Codec[Array[Byte]] {
    def serialize(value: Array[Byte]): Array[Byte] = value
    def deserialize(data: Array[Byte]): Array[Byte] = data
  }
  implicit object SessionBinaryCodec extends Codec[SessionDto] {
    def serialize(value: SessionDto): Array[Byte] = toBytes(value, SessionDto.getClassSchema)
    def deserialize(data: Array[Byte]): SessionDto = fromBytes(data, SessionDto.getClassSchema)
  }

  implicit object SpecificArrayBinaryCodec extends Codec[SpecificArrayDto] {
    def serialize(value: SpecificArrayDto): Array[Byte] = toBytes(value, SpecificArrayDto.getClassSchema)
    def deserialize(data: Array[Byte]): SpecificArrayDto = fromBytes(data, SpecificArrayDto.getClassSchema)
  }

  implicit object UserBinaryCodec extends Codec[UserDto] {
    def serialize(value: UserDto): Array[Byte] = toBytes(value, UserDto.getClassSchema)
    def deserialize(data: Array[Byte]): UserDto = fromBytes(data, UserDto.getClassSchema)
  }
  implicit object TokenBinaryCodec extends Codec[TokenDto] {
    def serialize(value: TokenDto): Array[Byte] = toBytes(value, TokenDto.getClassSchema)
    def deserialize(data: Array[Byte]): TokenDto = fromBytes(data, TokenDto.getClassSchema)
  }

  implicit object LoginBinaryCodec extends Codec[LoginDto] {
    def serialize(value: LoginDto): Array[Byte] = toBytes(value, LoginDto.getClassSchema)
    def deserialize(data: Array[Byte]): LoginDto = fromBytes(data, LoginDto.getClassSchema)
  }

  implicit object CartBinaryCodec extends Codec[CartDto] {
    def serialize(value: CartDto): Array[Byte] = toBytes(value, CartDto.getClassSchema)
    def deserialize(data: Array[Byte]): CartDto = fromBytes(data, CartDto.getClassSchema)
  }
  // implicit object AuctionBinaryCodec extends Codec[AuctionDto] {
  //   def serialize(value: AuctionDto): Array[Byte] = toBytes(value, AuctionDto.getClassSchema)
  //   def deserialize(data: Array[Byte]): AuctionDto = fromBytes(data, AuctionDto.getClassSchema)
  // }

  /**
   *
   * @param a class to serialize
   * @param schema usually should be something like
   * @tparam A avro class representative
   * @return serialized to array of byte
   */
  def toBytes[A](a: A, schema: => org.apache.avro.Schema) = {
    val out = new ByteArrayOutputStream()
    val encoder = EncoderFactory.get().binaryEncoder(out, null)
    val writer = new SpecificDatumWriter[A](schema)
    writer.write(a, encoder)
    encoder.flush()
    out.close()
    out.toByteArray
  }  
    def fromBytes[A](bytes: Array[Byte], schema: => org.apache.avro.Schema) = {
    val reader = new SpecificDatumReader[A](schema)
    val inputStream = new SeekableByteArrayInput(bytes)

    val decoder = DecoderFactory.get.binaryDecoder(inputStream, null)
    reader.read(null.asInstanceOf[A], decoder)
  }                              
}