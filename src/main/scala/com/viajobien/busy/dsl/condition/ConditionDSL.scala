package com.viajobien.busy.dsl.condition

import scala.util.Try
import scala.util.parsing.combinator.syntactical._

/**
 * @author david on 15/11/14.
 */
object ConditionDSL {

  /**
   * @see http://debasishg.blogspot.com.ar/2008/04/external-dsls-made-easy-with-scala.html
   */
  object Parser extends StandardTokenParsers {

    val ALWAYS = "always"
    val HEADER = "header"
    val QUERY = "query"
    val JSON = "json"
    val BODY = "body"
    val NAME = "name"
    val PARAMETER = "parameter"
    val IS = "is"
    val CONTAINS = "contains"
    val STARTS = "starts"
    val ENDS = "ends"
    val WITH = "with"

    lexical.reserved += (ALWAYS, HEADER, QUERY, JSON, BODY, NAME, PARAMETER, IS, CONTAINS, STARTS, ENDS, WITH)

    def transform(dsl: String): ParseResult[Condition] = this.trans(new lexical.Scanner(dsl.trim))

    private def trans: Parser[Condition] =
      condition ~ (checkField ?) ~ (checkValue ?) ^^ {
        case (a: Always) ~ _ ~ _                                        => a
        case (h: Header) ~ Some((NAME, n)) ~ Some(Is(v))                => h name n is v
        case (q: Query) ~ Some((PARAMETER, n)) ~ Some(Is(v))            => q parameter n is v
        case (b: JsonBody) ~ Some((PARAMETER, n)) ~ Some(Is(v))         => b parameter n is v
        case (b: JsonBody) ~ Some((PARAMETER, n)) ~ Some(StartsWith(v)) => b parameter n startsWith v
        case (b: JsonBody) ~ Some((PARAMETER, n)) ~ Some(EndsWith(v))   => b parameter n endsWith v
      }

    private def condition: Parser[Condition] =
      (ALWAYS | HEADER | QUERY | (JSON <~ BODY)) ^^ {
        case ALWAYS => Always()
        case HEADER => Header()
        case QUERY  => Query()
        case JSON   => JsonBody()
      }

    private def checkField: Parser[(String, String)] =
      (NAME | PARAMETER) ~ stringLit ^^ { case n ~ s => (n, s) }

    private def checkValue: Parser[Evaluator] =
      (IS | CONTAINS | (STARTS <~ WITH) | (ENDS <~ WITH)) ~ stringLit ^^ {
        case IS ~ v       => Is(v)
        case CONTAINS ~ v => Contains(v)
        case STARTS ~ v   => StartsWith(v)
        case ENDS ~ v     => EndsWith(v)
        case _            => True()
      }

  }

  implicit class ConditionParser(s: String) {

    def parseCondition: Try[Condition] = Try {
      Parser.transform(s).get
    }

  }

}
