package com.viajobien.busy.models

/**
 * Provides id type.
 * @author david on 13/10/14.
 */
trait Persistable {
  type T
  val id: T
}
