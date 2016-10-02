package com.viajobien.busy.dsl.condition

import org.scalatest.{ Matchers, WordSpecLike }

/**
 * @author david on 24/11/14.
 */
class ConditionTest extends WordSpecLike with Matchers {

  "Condition#toString" must {

    "on Always return always" in {
      val cond = Always()

      cond.toString shouldBe "always"
    }

    "on Header" in {
      val cond = Header(Some("xx"), Is("yy"))

      cond.toString shouldBe """header name "xx" is "yy" """
    }

    "on Query" in {
      val cond = Query(Some("xx"), Is("yy"))

      cond.toString shouldBe """query parameter "xx" is "yy" """
    }

    "on JsonBody" in {
      val cond1 = JsonBody(Some("x1"), Is("y1"))
      val cond2 = JsonBody(Some("x2"), StartsWith("y2"))
      val cond3 = JsonBody(Some("x3"), EndsWith("y3"))

      cond1.toString shouldBe """json body parameter "x1" is "y1" """
      cond2.toString shouldBe """json body parameter "x2" starts with "y2" """
      cond3.toString shouldBe """json body parameter "x3" ends with "y3" """
    }

  }

}
