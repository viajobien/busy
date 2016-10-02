package com.viajobien.busy.dsl.condition

import ConditionDSL.Parser
import org.scalatest.{ Matchers, WordSpecLike }

/**
 * @author david on 16/11/14.
 */
class ParserTest extends WordSpecLike with Matchers {

  "Parser.transform" must {

    "return a Header instance with Is evaluator" in {
      val dsl = """ header name "to" is "david" """

      val cond = Parser.transform(dsl)

      cond.get shouldBe Header(Some("to"), Is("david"))
    }

    "return a JsonBody instance with Is evaluator" in {
      val dsl = """ json body parameter "url" is "http://sarasa.com" """

      val cond = Parser.transform(dsl)

      cond.get shouldBe JsonBody(Some("url"), Is("http://sarasa.com"))
    }

    "return a JsonBody instance with StartsWith evaluator" in {
      val dsl = """ json body parameter "url" starts with "http://sarasa.com" """

      val cond = Parser.transform(dsl)

      cond.get shouldBe JsonBody(Some("url"), StartsWith("http://sarasa.com"))
    }

    "return a JsonBody instance with EndsWith evaluator" in {
      val dsl = """ json body parameter "url" ends with "http://sarasa.com" """

      val cond = Parser.transform(dsl)

      cond.get shouldBe JsonBody(Some("url"), EndsWith("http://sarasa.com"))
    }

    "return a Always instance" in {
      val dsl = """ always """

      val cond = Parser.transform(dsl)

      cond.get shouldBe Always()
    }

  }

}
