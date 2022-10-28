package com.nichoshop.models.helpers

import com.nichoshop.Environment
import com.nichoshop.Environment.driver.simple._

/**
 * Created by Evgeny Zhoga on 14.06.15.
 */
object DB {
  def read[E]( f: Session => E ):E = {
    Environment.db.withSession { session:Session =>
      session.conn.setReadOnly(true)
      f(session)
    }
  }
  def write[E]( f: Session => E ):E = {
    Environment.db.withSession { session:Session =>
      session.conn.setReadOnly(false)
      f(session)
    }
  }

  def transaction[E]( f: Session => E ):E = {
    Environment.db.withTransaction { session:Session =>
      session.conn.setReadOnly(false)
      f(session)
    }
  }

}
