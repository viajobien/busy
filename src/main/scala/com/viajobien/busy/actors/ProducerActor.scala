package com.viajobien.busy.actors

import javax.inject.{ Inject, Named }

import akka.actor.{ Actor, ActorRef }
import play.api.Logger
import play.api.http.{ HeaderNames, MimeTypes }
import play.api.libs.json.{ JsNull, JsValue, Json }
import play.api.libs.ws.{ WSClient, WSRequest, WSResponse }
import play.api.mvc.{ AnyContent, Headers, Request }

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success }

/**
 * Probably, in the future, this would be a simple class, not an actor, or some akka-stream wrapper.
 *
 * @author david on 13/10/14.
 */
class ProducerActor @Inject() (
    ws: WSClient,
    @Named(ProducerActor.contextName) implicit val ec: ExecutionContext
) extends Actor {

  def receive: Receive = {
    case Produce(req, uri, duration) =>
      val senderRef = sender()
      val request = req.asInstanceOf[Request[AnyContent]]

      val url =
        if (request.queryString.nonEmpty) s"$uri?${request.rawQueryString}"
        else uri

      val headers = this.mkHeaders(request.headers)
      val body = request.body.asJson.getOrElse(Json.obj())

      Logger.trace(s"URL: $url / Method: ${request.method} / Headers: $headers / Body: $body")

      val wsRequest = this.buildWSRequest(url, headers, duration, body, request)
      val futureResponse = wsRequest.execute()

      this.processResponse(futureResponse, senderRef)
  }

  private def buildWSRequest(url: String, headers: Map[String, String],
    duration: FiniteDuration, body: JsValue,
    method: Request[AnyContent]): WSRequest =
    this.ws.url(url)
      .withHeaders(headers.toList: _*)
      .withRequestTimeout(duration)
      .withFollowRedirects(follow = true)
      .withBody(body)
      .withMethod(method.method)

  private def mkHeaders(defaults: Headers): Map[String, String] =
    defaults.toSimpleMap +
      (HeaderNames.ACCEPT -> MimeTypes.JSON) +
      (HeaderNames.CONTENT_TYPE -> s"${MimeTypes.JSON};charset=utf-8") -
      HeaderNames.CONNECTION -
      HeaderNames.HOST

  private def processResponse(futureRes: Future[WSResponse], senderRef: ActorRef) =
    futureRes map (_.json) onComplete {
      case Success(json) =>
        Logger.trace(s"remote response is $json")
        senderRef ! json
      case Failure(e) =>
        Logger.warn("failure getting response", e)
        senderRef ! JsNull
    }

}

object ProducerActor {
  /*
   * Final vals to bind by name (dependency injection)
   */
  final val contextName = "ProducerContext"
  final val name = "ProducerActor"
}
