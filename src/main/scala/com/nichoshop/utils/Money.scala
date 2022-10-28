package com.nichoshop.utils

/**
 * Created by Evgeny Zhoga on 10.10.15.
 */
class Money(amount: Int, base: Int = 100) {
  def high = amount / base
  def low = amount % base
}

object Money {
  implicit def int2money(amount: Int): Money = new Money(amount)
}
