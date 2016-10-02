package com.viajobien.busy.models.routing

import com.viajobien.busy.dsl.condition.{ Always, Condition }
import org.scalatest.{ Matchers, WordSpecLike }
import play.api.mvc.AnyContentAsEmpty
import play.api.test.{ FakeHeaders, FakeRequest }

/**
 * @author david on 13/03/15.
 */
class RouteTreeSpec extends WordSpecLike with Matchers {

  "find" must {

    "return an option RouteTable" in {
      val r1 = RouteImpl().copy(path = "/one/two")
      val r2 = RouteImpl().copy(path = "/one/two/three")
      val r3 = RouteImpl().copy(path = "/one/two/three")
      val r4 = RouteImpl().copy(path = "/one/two/*/dd")
      val r5 = RouteImpl().copy(path = "/one/two/*/dd/**")
      val r6 = RouteImpl().copy(path = "/one/two/*/dd/dc/a")

      val builder = new RouteBuilder(List(r1, r2, r3, r4, r5, r6))

      val tree = builder.createRouteTree

      val rq1 = FakeRequest("GET", "/", FakeHeaders(), AnyContentAsEmpty)
      val rq2 = FakeRequest("GET", "/one/two", FakeHeaders(), AnyContentAsEmpty)
      val rq3 = FakeRequest("GET", "/one/two/three", FakeHeaders(), AnyContentAsEmpty)
      val rq4 = FakeRequest("GET", "/one/two/3/dd", FakeHeaders(), AnyContentAsEmpty)
      val rq5 = FakeRequest("GET", "/one/two/3/dd/xx", FakeHeaders(), AnyContentAsEmpty)
      val rq6 = FakeRequest("GET", "/one/two/3/dd/xx/a", FakeHeaders(), AnyContentAsEmpty)
      val rq7 = FakeRequest("GET", "/one/two/3/dd/dc/a", FakeHeaders(), AnyContentAsEmpty)

      tree.find(rq1) shouldBe None
      tree.find(rq2) shouldBe Some(r1)
      tree.find(rq3) shouldBe Some(r2)
      tree.find(rq4) shouldBe Some(r4)
      tree.find(rq5) shouldBe Some(r5)
      tree.find(rq6) shouldBe Some(r5)
      tree.find(rq7) shouldBe Some(r5)
    }

  }

  case class RouteImpl(
      override val path: String = "path",
      endpoint: String = "endpoint",
      condition: Condition = Always()
  ) extends Route(path, endpoint, condition) {

    override type T = String
    override val id: String = "id"

  }

}
