package com.nichoshop.utils

import java.security.{MessageDigest, SecureRandom}

import java.time.Instant
import java.util.{Date, UUID}
import java.text.SimpleDateFormat

import io.jsonwebtoken.{Claims, Jws, Jwts, SignatureAlgorithm, JwtException}
import scala.util.control.NonFatal
import java.util.regex._; 

object SecureUtils {
  val TOKEN_LENGTH = 45 // TOKEN_LENGTH is not the return size from a hash,
  // but the total characters used as random token prior to hash
  // 45 was selected because System.nanoTime().toString returns
  // 19 characters. 45 + 19 = 64. Therefore we are guaranteed
  // at least 64 characters (bytes) to use in hash, to avoid MD5 collision < 64

  val TOKEN_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_.-"
  val secureRandom = new SecureRandom()

  private def toHex(bytes: Array[Byte]): String = bytes.map("%02x".format(_)).mkString("")

  private def sha(s: String): String = {
    toHex(MessageDigest.getInstance("SHA-256").digest(s.getBytes("UTF-8")))
  }

  private def md5(s: String): String = {
    toHex(MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8")))
  }

  private def generateToken(tokenLength: Int): String = {
    val l = TOKEN_CHARS.length()

    def loop(acc: String, i: Int): String = {
      if (i == 0) acc
      else loop(acc + TOKEN_CHARS(secureRandom.nextInt(l)), i - 1)
    }
    loop("", tokenLength)
  }

  /*
  * Hash the Token to return a 32 or 64 character HEX String
  *
  * Parameters:
  * tokenprefix: string to concatenate with random generated token prior to HASH to improve uniqueness, such as username
  *
  * Returns:
  * MD5 hash of (username + current time + random token generator) as token, 128 bits, 32 characters
  * or
  * SHA-256 hash of (username + current time + random token generator) as token, 256 bits, 64 characters
  */
  def generateMD5Token(tokenprefix: String): String = {
    md5(tokenprefix + System.nanoTime() + generateToken(TOKEN_LENGTH))
  }

  def generateSHAToken(tokenprefix: String): String = {
    sha(tokenprefix + System.nanoTime() + generateToken(TOKEN_LENGTH))
  }

  def generateSMSCode(): String = {        
    (100000 + secureRandom.nextInt(900000)).toString()    
  }

  def generateJWTToken(tokenSecret: String, tokenExpire: Int, claims : Map[String, Any]) : String = {
    val jwt = Jwts.builder()
      .setId(UUID.randomUUID.toString)
      .setIssuedAt(Date.from(Instant.now()))
      .setExpiration(Date.from(Instant.now().plusSeconds(tokenExpire)))   // only available for 24hours 
      .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes("UTF-8"))

     claims.foreach { case (name, value) =>
        jwt.claim(name, value)
      }
    jwt.compact()
  }

  def verifyJWTToken(tokenSecret: String, token : String) = {
    
    try {
      val claims : Claims = Jwts.parser()
        .setSigningKey(tokenSecret.getBytes("UTF-8"))
        .parseClaimsJws(token) // we can trust this JWT
        .getBody()

      Map(  "id"->claims.get("id"),
            "username"->claims.get("username"),
            "email"-> claims.get("email"))
      } catch {
        case e: JwtException => throw new Exception("Token is Invalid")
        case NonFatal(e) => throw new Exception("Token parameter is not enough")
      }
  }

  def generateSUC(usedSUC:List[String]): String = {
    var sucList =(100000 to 999999)

    usedSUC.foreach{ s => {
      val idx = sucList.indexOf(s.toInt)
      if(idx>0)
        sucList.zipWithIndex.filter(_._1 != idx).map(_._2)
    }}

    sucList(secureRandom.nextInt(sucList.size)).toString
    
  }

  def generateUsername(fullname: String = "", countryCode: String = "US"): String = { 
    /**
     * First 6 letters of the last name / family + dot + the first 4 letters of the first name
     * + 5 random numbers + the symbols of their country, for example "abcdef.ghij13254gb".
     * If the first name is shorter than 4 letters, in this case less than 4 letters of the name will be used 
     */
        val names = fullname.split(" ")
        var fname = names(0)
        var lname = names(1)
        if(fname == null || lname == null) return ""

      val randomNum = (10000 + secureRandom.nextInt(90000)).toString()    
      lname =  if(lname.length > 5) lname.substring(0, 6) else lname
      fname = if(fname.length > 3) fname.substring(0,4) else fname
      val username: String = lname + "." + fname + randomNum + countryCode.toLowerCase
      username
  }

  def generateOrderId(serialNum: Int) : String = {
      /**
       * Order ID - the identification number of this order will be displayed.
       * The system will generate a unique identification number for each order.
       * The order number is composed of 14 digits, the first 8 digits are the serial number of the order for that day (starting from 00000001),
       * then a dash and the next 6 digits, which are DD MM YY i.e. the day, month and year when this order was placed, for example 00000001-010121.
       */
      val dNow = new Date();
      val ft = new SimpleDateFormat ("DDMMYY");

      val dateString = ft.format(dNow).toString

      def loop(acc: String, i: Int = 0): String = {
        if (i == 0) acc
        else loop("0"+ acc)
      }     
      var serialString = serialNum.toString()

      serialString = if(serialString.length > 7) serialString.substring(0,8)
                        else {
                          val num = 8 - serialString.length
                          loop(serialString, num)
                        }
      val orderId = serialString + dateString
       orderId
  }

  def day2time() : Long = {
      val dateObj = new Date()
      val newObj = new Date(dateObj.getYear, dateObj.getMonth,dateObj.getDay(),0,0,1)
      newObj.getTime()
  }

  def year2time(year: Int) : Long = {
      val newObj = new Date(year, 1,1,0,0,1)
      newObj.getTime() 
  }

  def isValidPhoneNumber(phone: String) : Boolean = {
    //(0/91): number starts with (0/91)  
    //[7-9]: starting of the number may contain a digit between 0 to 9  
    //[0-9]: then contains digits 0 to 9  
    val ptrn : Pattern = Pattern.compile("^\\+?\\d{6,18}$")
    //the matcher() method creates a matcher that will match the given input against this pattern  
    val matche : Matcher = ptrn.matcher(phone)
    //returns a boolean value  
    return (matche.find() && matche.group().equals(phone))
  }

  def findGapNumber(numList: List[Int]) = {
    // var tempList : List[Int]
    // for(num <- numList.min() to numList.max() ) {
    //     if(numList.contains(num)) tempList += num
    // }
  }  

}