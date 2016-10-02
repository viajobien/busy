package com.viajobien.busy.models.routing

import scala.collection.immutable.TreeMap

/**
 * @author david on 14/03/15.
 */
class RouteBuilder(routeTables: List[Route]) extends PathUtil {

  def createRouteTree: RouteTree = routeTables.foldLeft[RouteTree](
    new RouteEmpty()
  ) { (rn, rt) =>
      rn ++ this.buildTree(rt, this.splitPath(rt.path))
    }

  private def buildTree(routeTable: Route, parts: List[String] = Nil): RouteTree =
    parts match {
      case Nil                 => new RouteEndpointNode(List(routeTable))
      case PATH_WILDCARD :: ps => new RouteNode(TreeMap(PATH_WILDCARD -> new RouteWildcardNode(List(routeTable))))
      case p :: ps             => new RouteNode(TreeMap(p -> this.buildTree(routeTable, ps)))
    }

}
