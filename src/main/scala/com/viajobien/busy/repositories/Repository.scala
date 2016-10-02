package com.viajobien.busy.repositories

import com.viajobien.busy.models.Persistable

import scala.concurrent.{ ExecutionContext, Future }

/**
 * @author LeonhardtDavid on 11/9/16.
 */
trait Repository[P <: Persistable] {

  val ec: ExecutionContext

  def save(item: P): Future[P]

  def update(item: P): Future[P]

  def remove(id: P#T): Future[Boolean]

  def findById(id: P#T): Future[Option[P]]

  def findAll(): Future[List[P]]

  def count: Future[Int]

}
