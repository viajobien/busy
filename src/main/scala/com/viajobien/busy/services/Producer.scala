package com.viajobien.busy.services

import javax.inject.{ Inject, Named }

import play.api.Logger
import play.api.http.{ HeaderNames, MimeTypes }
import play.api.libs.json.{ JsNull, JsValue, Json }
import play.api.libs.ws.{ WSClient, WSRequest, WSResponse }
import play.api.mvc.{ AnyContent, Headers, Request }

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ ExecutionContext, Future }

class Producer @Inject() (ws: WSClient, @Named(Producer.contextName) implicit val ec: ExecutionContext) {

  def produce[A](request: A, uri: String, duration: FiniteDuration): Future[JsValue] = {
    val req = request.asInstanceOf[Request[AnyContent]]

    val url =
      if (req.queryString.nonEmpty) s"$uri?${req.rawQueryString}"
      else uri

    val headers = this.mkHeaders(req.headers)
    val body = req.body.asJson.getOrElse(Json.obj())

    Logger.trace(s"URL: $url / Method: ${req.method} / Headers: $headers / Body: $body")

    val wsRequest = this.buildWSRequest(url, headers, duration, body, req.method)
    val futureResponse = wsRequest.execute()

    this.processResponse(futureResponse)
  }

  private def buildWSRequest(url: String, headers: Map[String, String],
    duration: FiniteDuration, body: JsValue,
    method: String): WSRequest =
    this.ws.url(url)
      .withHttpHeaders(headers.toList: _*)
      .withRequestTimeout(duration)
      .withFollowRedirects(follow = true)
      .withBody(body)
      .withMethod(method)

  private def mkHeaders(defaults: Headers): Map[String, String] =
    defaults.toSimpleMap +
      (HeaderNames.ACCEPT -> MimeTypes.JSON) +
      (HeaderNames.CONTENT_TYPE -> s"${MimeTypes.JSON};charset=utf-8") -
      HeaderNames.CONNECTION -
      HeaderNames.HOST

  private def processResponse(futureRes: Future[WSResponse]) = {
    val futureJson = futureRes map (_.json) recover {
      case e =>
        Logger.warn("failure getting response", e)
        JsNull
    }

    futureJson foreach { json =>
      Logger.trace(s"remote response is $json")
    }

    futureJson
  }

}

object Producer {
  /*
   * Final vals to bind by name (dependency injection)
   */
  final val contextName = "ProducerContext"
}
