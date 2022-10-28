package com.nichoshop.models.helpers

/**
 * Created by Evgeny Zhoga on 24.10.15.
 */
object Currencies {
  sealed trait Currency {
    def name: String
    def base: Int = 100
    def code: Int
  }
  case object EURO extends Currency {
    val name = "Euro"
    val code = 978
  }
  case object DOLLAR extends Currency {
    val name = "Dollar"
    val code = 840
  }

  val all = List(EURO, DOLLAR)
  /** Code is supposed to be unique */
  def byCode(code: Int) = all.find(_.code == code)
  /** different currencies could have same name. Method is case insencitive */
  def byName(name: String) = all.filter(_.name.toLowerCase == name.toLowerCase)
}
