package com.viajobien.busy.actors

import scala.concurrent.duration.FiniteDuration

/**
 * Created by david on 29/06/16.
 */
trait Message

sealed trait ProducerMessage extends Message
case class Produce[A](request: A, uri: String, in: FiniteDuration) extends ProducerMessage
