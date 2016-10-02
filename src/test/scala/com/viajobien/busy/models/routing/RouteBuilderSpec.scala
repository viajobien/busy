package com.viajobien.busy.models.routing

import com.viajobien.busy.dsl.condition.{ Always, Condition }
import org.scalatest.{ Matchers, WordSpecLike }

/**
 * @author david on 13/03/15.
 */
class RouteBuilderSpec extends WordSpecLike with Matchers {

  "createRouteTree" must {

    "return a RouteTree" in {
      val r1 = RouteImpl().copy(path = "/one/two")
      val r2 = RouteImpl().copy(path = "/one/two/three")
      val r3 = RouteImpl().copy(path = "/one/two/three")
      val r4 = RouteImpl().copy(path = "/one/two/*/dd")
      val r5 = RouteImpl().copy(path = "/one/two/*/dd/**")
      val r6 = RouteImpl().copy(path = "/one/two/*/dd/dc/a")

      val builder = new RouteBuilder(List(r1, r2, r3, r4, r5, r6))

      val tree = builder.createRouteTree

      tree shouldNot be(null)
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
