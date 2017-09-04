package com.viajobien.busy.models.routing

import com.viajobien.busy.models.routing
import play.api.mvc.{ AnyContent, Request }

import scala.collection.immutable.TreeMap

/**
 * @author david on 13/03/15.
 */
sealed abstract class RouteTree extends PathUtil {

  /**
   * Find the proper [[routing.Route]] for a [[play.api.mvc.Request]]
   *
   * @param req a [[play.api.mvc.Request]] to find the route
   * @return a [[scala.Some]] of [[routing.Route]] if there is someone that match, None otherwise
   */
  def find(req: Request[AnyContent]): Option[Route] = {
    val parts = this.splitPath(req.path)
    val tables = this.find(parts)

    tables find (_.satisfy(req))
  }

  /**
   * Find the first [[routing.Route ]] that match [[routing.Route#satisfy]]
   *
   * @param parts the path parts to use to find what rote match, must not be empty
   * @return a Some[Route] if there is someone that match, None otherwise
   */
  def find(parts: List[String]): List[Route]

  /**
   * Merge two trees
   * @param that a tree to merge with
   * @return
   */
  def ++(that: RouteTree): RouteTree

  /**
   * Enrich this tree with a new one.
   *
   * @param thatMap     a map representing the tree used to enrich this
   * @param routes a list of [[routing.Route]] that must be in this node
   * @return a new [[RouteTree]]
   */
  def enrich(thatMap: Map[String, RouteTree], routes: List[Route]): RouteTree =
    routes match {
      case Nil => new RouteNode(thatMap)
      case _   => new RouteEndpointNode(routes, thatMap)
    }

  override def toString: String

}

class RouteEmpty() extends RouteTree {

  def find(parts: List[String]): List[Route] = Nil

  def ++(that: RouteTree): RouteTree = that

  override def toString: String = "{}"

}

/**
 *
 * @param mapTree
 */
class RouteNode(val mapTree: Map[String, RouteTree] = TreeMap.empty[String, RouteTree]) extends RouteTree {

  def find(parts: List[String]): List[Route] =
    parts match {
      case Nil => Nil
      case p :: ps =>
        val tree = this.mapTree.getOrElse(
          PATH_WILDCARD,
          this.mapTree.getOrElse(
            p,
            this.mapTree.getOrElse(PATH_PART_WILDCARD, new RouteEmpty())))
        tree.find(ps)
    }

  def ++(that: RouteTree): RouteTree = that.enrich(this.mapTree, Nil)

  override def enrich(thatMap: Map[String, RouteTree], routes: List[Route]): RouteTree = {
    val map = this.mergeMaps(thatMap)
    super.enrich(map, routes)
  }

  /**
   * Merge this.mapTree with another one
   *
   * @param thatMap a map to merge in this
   * @return the map resulting of merge
   */
  protected def mergeMaps(thatMap: Map[String, RouteTree]): Map[String, RouteTree] = {
    val map = thatMap map {
      case (part, tree) =>
        val newTree = this.mapTree.getOrElse(part, new RouteEmpty) ++ tree
        part -> newTree
    }
    this.mapTree ++ map
  }

  override def toString: String = s"""${this.getClass.getSimpleName}(
                                     | ${this.mapTree.toString()}
                                     |)""".stripMargin

}

/**
 *
 * @param routes the [[routing.Route]]s that are in this node
 */
class RouteEndpointNode(
    routes: List[Route],
    mapTree: Map[String, RouteTree] = TreeMap.empty[String, RouteTree])
  extends RouteNode(mapTree) {

  require(routes.nonEmpty)

  override def find(parts: List[String]): List[Route] = parts match {
    case Nil => this.routes
    case _   => super.find(parts)
  }

  override def ++(that: RouteTree): RouteTree = that.enrich(this.mapTree, this.routes)

  override def enrich(thatMap: Map[String, RouteTree], routes: List[Route]): RouteTree = {
    val map = this.mergeMaps(thatMap)
    new RouteEndpointNode(this.routes ++ routes, map)
  }

  override def toString: String =
    s"""${this.getClass.getSimpleName}(
       | { routes => ${this.routes.toString()},
       |  ${this.mapTree}
       | }
       |)""".stripMargin

}

/**
 *
 * @param routes the [[routing.Route]]s that are in this node
 */
class RouteWildcardNode(routes: List[Route]) extends RouteEndpointNode(routes) {

  override def find(parts: List[String]): List[Route] = this.routes

  override def enrich(thatMap: Map[String, RouteTree], routes: List[Route]): RouteTree =
    new RouteWildcardNode(this.routes ++ routes)

}
