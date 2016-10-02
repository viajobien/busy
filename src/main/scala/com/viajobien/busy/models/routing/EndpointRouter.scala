package com.viajobien.busy.models.routing

import com.viajobien.busy.repositories.Repository
import play.api.Logger
import play.api.mvc.{ AnyContent, Request }

import scala.concurrent.duration._
import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.util.{ Failure, Success, Try }

/**
 * Router interface.
 */
trait EndpointRouter {

  type Endpoint = String

  /**
   * Returns an Option[Endpoint] for a given Request.
   */
  def route(implicit req: Request[AnyContent]): Option[Endpoint]

  /**
   * Reload routes.
   */
  def reload(implicit ec: ExecutionContext): Future[Try[_]]

}

/**
 * Basic router implementation.
 */
class BasicRouter[T <: Route](repository: Repository[T]) extends EndpointRouter {

  /**
   * Tree of available routes.
   */
  var routeTree: RouteTree = new RouteBuilder(List.empty[Route]).createRouteTree

  /**
   * Find all routes and builds a new tree.
   */
  def reload(implicit ec: ExecutionContext): Future[Try[_]] =
    this.repository.findAll() map { routes =>
      Logger.debug("configuring routes: " + routes)
      val builder = new RouteBuilder(routes)
      this.routeTree = builder.createRouteTree
      Success("ok")
    } recover {
      case e =>
        Failure(e)
    }

  /**
   * Searches and endpoint using request's path.
   */
  override def route(implicit req: Request[AnyContent]): Option[Endpoint] = {
    Logger.trace("routing " + req + " with routeTree " + routeTree)
    this.routeTree.find(req) match {
      case Some(rt) => Some(rt.generateEndpoint(req.path))
      case _        => None
    }
  }

}

object BasicRouter {

  var router: Option[EndpointRouter] = None

  def reload[T <: Route](repository: Repository[T])(implicit ec: ExecutionContext): EndpointRouter = {
    val basicRouter = new BasicRouter(repository)
    Await.ready(basicRouter.reload, 5.seconds)
    this.router = Some(basicRouter)
    basicRouter
  }

  def getRouter[T <: Route](repository: Repository[T])(implicit ec: ExecutionContext): EndpointRouter = {
    if (this.router.isEmpty) {
      this.reload(repository)
    }
    this.router.get
  }

}
