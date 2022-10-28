package com.nichoshop.services

import com.nichoshop.legacy.dao.GenericDAO

trait CommonService[T] {

  def dao: GenericDAO[T]

  def findById(id: Int): Option[T] = dao.findById(id)
  def findAll: List[T] = dao.findAll
  def create(x: T) = dao.create(x)
  def updateById(id: Int, x: T) = dao.updateById(id, x)
  def deleteById(id: Int) = dao.deleteById(id)
}