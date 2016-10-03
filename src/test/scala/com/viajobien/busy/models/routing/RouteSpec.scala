package com.viajobien.busy.models.routing

import com.viajobien.busy.dsl.condition.{ Always, Condition }
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatest.{ Matchers, WordSpecLike }
import play.api.mvc.{ AnyContent, Request }
import play.api.test.FakeRequest

/**
 * author LeonhardtDavid on 2/10/16.
 */
class RouteSpec extends WordSpecLike with Matchers with MockitoSugar {

  "A Route" must {

    "satisfy a condition calling eval method" in {
      val condition = mock[Condition]
      val route = RouteImpl(condition = condition)

      when(condition.eval(any[Request[AnyContent]])).thenReturn(true)

      val request = FakeRequest()
      val satisfy = route.satisfy(request)

      satisfy shouldBe true
      verify(condition, times(1)).eval(request)
    }

    "generateEndpoint" in {
      val route = RouteImpl("/public/*", "http://test.me/*/x", mock[Condition])
      val endpoint = route.generateEndpoint("/public/test")

      endpoint shouldBe "http://test.me/test/x"
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
