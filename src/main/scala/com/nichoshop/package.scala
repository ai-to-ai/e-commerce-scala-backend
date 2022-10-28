package com

import scala.concurrent.duration.FiniteDuration

/**
 * Created by Evgeny Zhoga on 23.09.15.
 */
package object nichoshop {
  implicit def duration2long(d: FiniteDuration): Long = d.toMillis
}
