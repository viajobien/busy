package com.viajobien.busy.dsl.condition

import org.scalatest.{ Matchers, WordSpecLike }
import play.api.libs.json.Json
import play.api.mvc.{ AnyContentAsEmpty, AnyContentAsJson, Headers }
import play.api.test.FakeRequest

/**
 * @author david on 24/11/14.
 */
class ConditionSpec extends WordSpecLike with Matchers {

  "Always" must {

    val cond = Always()

    "evaluate to true" in {
      cond.evaluator shouldBe True()
    }

    "has no value" in {
      cond.value(FakeRequest()) shouldBe None
    }

    "eval to true" in {
      cond.eval(FakeRequest()) shouldBe true
    }

    "to string is always" in {
      cond.toString shouldBe "always"
    }

  }

  "Header" must {

    val cond = Header(Some("xx"), Is("yy"))

    "create new Header for a given name" in {
      cond.name("aa") shouldBe Header(Some("aa"), Is(""))
    }

    "create new Header for a given is evaluator" in {
      cond.is("bb") shouldBe Header(Some("xx"), Is("bb"))
    }

    "evaluate is Is" in {
      cond.evaluator shouldBe Is("yy")
    }

    "has no value if header xx not present" in {
      cond.value(FakeRequest()) shouldBe None
    }

    "has a value if header xx is present" in {
      cond.value(FakeRequest("GET", "/public/test", Headers("xx" -> "cc"), AnyContentAsEmpty)) shouldBe Some("cc")
    }

    "implement to string" in {
      cond.toString shouldBe """header name "xx" is "yy" """
    }

  }

  "Query" must {

    val cond = Query(Some("xx"), Is("yy"))

    "create new Query for a given parameter" in {
      cond.parameter("aa") shouldBe Query(Some("aa"), Is(""))
    }

    "create new Query for a given is evaluator" in {
      cond.is("bb") shouldBe Query(Some("xx"), Is("bb"))
    }

    "evaluate is Is" in {
      cond.evaluator shouldBe Is("yy")
    }

    "has no value if query parameter xx not present" in {
      cond.value(FakeRequest()) shouldBe None
    }

    "has a value if query parameter xx is present" in {
      cond.value(FakeRequest("GET", "/public/test?xx=cc")) shouldBe Some("cc")
    }

    "implement to string" in {
      cond.toString shouldBe """query parameter "xx" is "yy" """
    }

  }

  "JsonBody" must {

    val cond1 = JsonBody(Some("x1"), Is("y1"))
    val cond2 = JsonBody(Some("x2"), StartsWith("y2"))
    val cond3 = JsonBody(Some("x3"), EndsWith("y3"))

    "create new JsonBody for a given parameter" in {
      cond1.parameter("aa") shouldBe JsonBody(Some("aa"), Is(""))
    }

    "create new JsonBody for a given evaluator" in {
      cond1.is("bb") shouldBe JsonBody(Some("x1"), Is("bb"))
      cond1.startsWith("bb") shouldBe JsonBody(Some("x1"), StartsWith("bb"))
      cond1.endsWith("bb") shouldBe JsonBody(Some("x1"), EndsWith("bb"))
    }

    "evaluator" in {
      cond1.evaluator shouldBe Is("y1")
      cond2.evaluator shouldBe StartsWith("y2")
      cond3.evaluator shouldBe EndsWith("y3")
    }

    "has no value if json element x1 not present" in {
      cond1.value(FakeRequest()) shouldBe None
    }

    "has a value if json element x1 is present" in {
      cond1.value(FakeRequest("POST", "/public/test", Headers(), AnyContentAsJson(Json.obj("x1" -> "cc")))) shouldBe Some("cc")
    }

    "implement to string" in {
      cond1.toString shouldBe """json body parameter "x1" is "y1" """
      cond2.toString shouldBe """json body parameter "x2" starts with "y2" """
      cond3.toString shouldBe """json body parameter "x3" ends with "y3" """
    }

  }

}
