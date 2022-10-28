package com.nichoshop.utils

import org.slf4j
import org.slf4j.LoggerFactory

trait Logger {
  protected val log: slf4j.Logger = LoggerFactory.getLogger(getClass)
}
