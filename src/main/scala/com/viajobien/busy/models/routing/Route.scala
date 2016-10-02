package com.viajobien.busy.models.routing

import com.viajobien.busy.dsl.condition.Condition
import com.viajobien.busy.models.Persistable
import play.api.mvc.{ AnyContent, Request }

abstract class Route(val path: String, endpoint: String,
    condition: Condition) extends Persistable with PathUtil {

  def satisfy(request: Request[AnyContent]): Boolean = {
    this.condition.eval(request)
  }

  def generateEndpoint(pathParameter: String): String = {
    val splittedPath = this.splitPath(this.path)
    val splittedPathParameter = this.splitPath(pathParameter, splittedPath.size)
    val zippedPath = splittedPath zip splittedPathParameter

    val wildcardsPath = zippedPath filter (z => z._1 == PATH_PART_WILDCARD || z._1 == PATH_WILDCARD)

    this.splittedEndpoint(wildcardsPath)
  }

  private def splittedEndpoint(wildcardsPath: List[(String, String)]): String = {
    def splitted(splittedEndpoint: List[String], wildcardsPath: List[(String, String)]): List[String] =
      splittedEndpoint match {
        case Nil                                        => Nil
        case (PATH_PART_WILDCARD | PATH_WILDCARD) :: es => wildcardsPath.head._2 :: splitted(es, wildcardsPath.tail)
        case e :: es                                    => e :: splitted(es, wildcardsPath)
      }

    splitted(this.splitPath(this.endpoint, dropChars = 0), wildcardsPath) mkString "/"
  }

}
