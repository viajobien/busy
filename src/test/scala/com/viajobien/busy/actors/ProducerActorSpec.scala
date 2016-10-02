package com.viajobien.busy.actors

import akka.actor.{ ActorRef, ActorSystem, Props }
import akka.testkit.{ ImplicitSender, TestKit }
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{ BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpecLike }
import play.api.http.HttpVerbs
import play.api.libs.json.{ JsValue, Json }
import play.api.libs.ws.{ WSClient, WSRequest, WSResponse }
import play.api.mvc.{ AnyContent, AnyContentAsJson, Headers, Request }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

/**
 * @author david on 29/06/16.
 */
class ProducerActorSpec extends TestKit(ActorSystem("test-system-producer-actor")) with ImplicitSender
    with WordSpecLike with Matchers with MockitoSugar
    with BeforeAndAfterEach with BeforeAndAfterAll {

  var wsClient: WSClient = _
  var actor: ActorRef = _

  override protected def beforeEach() = {
    val wsReq = mock[WSRequest]
    val wsRes = mock[WSResponse]
    val futureRes = Future.successful(wsRes)

    this.wsClient = mock[WSClient]
    this.actor = system.actorOf(Props(classOf[ProducerActor], this.wsClient, global))

    when(this.wsClient.url(anyString())) thenReturn wsReq
    when(wsReq.withHeaders(anyObject())) thenReturn wsReq
    when(wsReq.withRequestTimeout(anyObject())) thenReturn wsReq
    when(wsReq.withFollowRedirects(anyBoolean())) thenReturn wsReq
    when(wsReq.withBody(any[JsValue])(anyObject())) thenReturn wsReq
    when(wsReq.withMethod(anyString())) thenReturn wsReq
    when(wsReq.execute()) thenReturn futureRes
    when(wsRes.json).thenReturn(Json.obj())
  }

  override protected def afterEach() = {
    this.wsClient = null
    this.actor = null
  }

  override protected def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }

  "ProducerActor" must {

    "xx" in {
      val req = mock[Request[AnyContent]]
      val msg = Produce(req, "http://uri.com", 10 seconds)

      when(req.queryString) thenReturn Map.empty[String, Seq[String]]
      when(req.headers) thenReturn Headers()
      when(req.body) thenReturn AnyContentAsJson(Json.obj())
      when(req.method) thenReturn HttpVerbs.GET

      this.actor ! msg

      expectMsg(Json.obj())
    }

  }

}
