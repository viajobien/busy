package com.viajobien.busy.dsl.condition

import ConditionDSL.Parser._
import play.api.mvc.{ AnyContent, Request }

import scala.util.{ Success, Try }

/**
 * @author david on 15/11/14.
 */
sealed trait Condition {

  val evaluator: Evaluator

  def value(implicit request: Request[AnyContent]): Option[String]

  def eval(implicit request: Request[AnyContent]): Boolean = evaluator.eval(this)

}

sealed trait Evaluator {

  val value: String

  protected def eval(
    cond: Condition,
    f: String => Boolean)(implicit request: Request[AnyContent]): Boolean = cond.value match {
    case Some(v) => f(v)
    case _       => false
  }

  def eval(cond: Condition)(implicit request: Request[AnyContent]): Boolean

}

case class Always() extends Condition {
  override val evaluator: Evaluator = True()
  override def value(implicit request: Request[AnyContent]): Option[String] = None
  override def eval(implicit request: Request[AnyContent]): Boolean = true
  override def toString: String = ALWAYS
}

case class Header(paramName: Option[String] = None, evaluator: Evaluator = Is("")) extends Condition {

  def name(parameter: String): Header = Header(Some(parameter))

  def is(value: String): Header = Header(paramName, Is(value))

  override def value(implicit request: Request[AnyContent]): Option[String] = paramName match {
    case Some(pName) => request.headers.get(pName)
    case None        => None
  }

  override def toString: String = s"""$HEADER name "${this.paramName.getOrElse("")}" ${this.evaluator}"""

}

case class Query(paramName: Option[String] = None, evaluator: Evaluator = Is("")) extends Condition {

  def parameter(parameter: String): Query = Query(Some(parameter))

  def is(value: String): Query = Query(paramName, Is(value))

  override def value(implicit request: Request[AnyContent]): Option[String] = paramName match {
    case Some(pName) => request.getQueryString(pName)
    case None        => None
  }

  override def toString: String = s"""$QUERY parameter "${this.paramName.getOrElse("")}" ${this.evaluator}"""

}

case class JsonBody(paramName: Option[String] = None, evaluator: Evaluator = Is("")) extends Condition {

  def parameter(parameter: String): JsonBody = JsonBody(Some(parameter))

  def is(value: String): JsonBody = JsonBody(paramName, Is(value))

  def startsWith(value: String): JsonBody = JsonBody(paramName, StartsWith(value))

  def endsWith(value: String): JsonBody = JsonBody(paramName, EndsWith(value))

  override def value(implicit request: Request[AnyContent]): Option[String] = (request.body.asJson, paramName) match {
    case (Some(json), Some(pName)) =>
      Try {
        (json \ pName).as[String]
      } match {
        case Success(s) => Some(s)
        case _          => None
      }
    case _ => None
  }

  override def toString: String = s"""$JSON $BODY parameter "${this.paramName.getOrElse("")}" ${this.evaluator}"""

}

case class Is(value: String) extends Evaluator {

  override def eval(cond: Condition)(implicit request: Request[AnyContent]): Boolean = eval(cond, _ == value)

  override def toString: String = s"""$IS "${this.value}" """

}

case class Contains(value: String) extends Evaluator {

  override def eval(cond: Condition)(implicit request: Request[AnyContent]): Boolean = eval(cond, _.contains(value))

  override def toString: String = s"""$CONTAINS "${this.value}" """

}

case class StartsWith(value: String) extends Evaluator {

  override def eval(cond: Condition)(implicit request: Request[AnyContent]): Boolean = eval(cond, _.startsWith(value))

  override def toString: String = s"""$STARTS $WITH "${this.value}" """

}

case class EndsWith(value: String) extends Evaluator {

  override def eval(cond: Condition)(implicit request: Request[AnyContent]): Boolean = eval(cond, _.endsWith(value))

  override def toString: String = s"""$ENDS $WITH "${this.value}" """

}

case class True() extends Evaluator {

  override val value: String = ""

  override def eval(cond: Condition)(implicit request: Request[AnyContent]): Boolean = true

  override def toString: String = this.value

}
