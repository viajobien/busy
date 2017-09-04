package com.viajobien.busy.services

import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest._
import org.scalatest.mockito.MockitoSugar
import play.api.http.HttpVerbs
import play.api.libs.json.{ JsValue, Json }
import play.api.libs.ws.{ WSClient, WSRequest, WSResponse }
import play.api.mvc.{ AnyContent, AnyContentAsJson, Headers, Request }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }

/**
 * @author david on 29/06/16.
 */
class ProducerSpec extends FlatSpec with Matchers with BeforeAndAfterEach with MockitoSugar {

  var wsClient: WSClient = _
  var producer: Producer = _

  override protected def beforeEach() = {
    val wsReq = mock[WSRequest]
    val wsRes = mock[WSResponse]
    val futureRes = Future.successful(wsRes)

    this.wsClient = mock[WSClient]
    this.producer = new Producer(this.wsClient, global)

    when(this.wsClient.url(anyString())) thenReturn wsReq
    when(wsReq.withHttpHeaders(any())) thenReturn wsReq
    when(wsReq.withRequestTimeout(any())) thenReturn wsReq
    when(wsReq.withFollowRedirects(anyBoolean())) thenReturn wsReq
    when(wsReq.withBody(any[JsValue])(any())) thenReturn wsReq
    when(wsReq.withMethod(anyString())) thenReturn wsReq
    when(wsReq.execute()) thenReturn futureRes
    when(wsRes.json).thenReturn(Json.obj())
  }

  override protected def afterEach() = {
    this.wsClient = null
    this.producer = null
  }

  "ProducerActor" should "produce request" in {
    val req = mock[Request[AnyContent]]

    when(req.queryString) thenReturn Map.empty[String, Seq[String]]
    when(req.headers) thenReturn Headers()
    when(req.body) thenReturn AnyContentAsJson(Json.obj())
    when(req.method) thenReturn HttpVerbs.GET

    val json = Await.result(this.producer.produce(req, "http://uri.com", 10 seconds), 10 seconds)

    json shouldBe Json.obj()
  }

}
