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

    // format: OFF
    val ALWAYS    = "always"
    val HEADER    = "header"
    val QUERY     = "query"
    val JSON      = "json"
    val BODY      = "body"
    val NAME      = "name"
    val PARAMETER = "parameter"
    val IS        = "is"
    val CONTAINS  = "contains"
    val STARTS    = "starts"
    val ENDS      = "ends"
    val WITH      = "with"
    // format: ON

    lexical.reserved += (ALWAYS, HEADER, QUERY, JSON, BODY, NAME, PARAMETER, IS, CONTAINS, STARTS, ENDS, WITH)

    def transform(dsl: String): ParseResult[Condition] = this.trans(new lexical.Scanner(dsl.trim))

    // format: OFF
    private def trans: Parser[Condition] =
      condition ~ (checkField ?) ~ (checkValue ?) ^^ {
        case (always: Always)     ~ _                      ~ _                   => always
        case (header: Header)     ~ Some((`NAME`, n))      ~ Some(Is(v))         => header name n is v
        case (query: Query)       ~ Some((`PARAMETER`, p)) ~ Some(Is(v))         => query parameter p is v
        case (jsonBody: JsonBody) ~ Some((`PARAMETER`, p)) ~ Some(Is(v))         => jsonBody parameter p is v
        case (jsonBody: JsonBody) ~ Some((`PARAMETER`, p)) ~ Some(StartsWith(v)) => jsonBody parameter p startsWith v
        case (jsonBody: JsonBody) ~ Some((`PARAMETER`, p)) ~ Some(EndsWith(v))   => jsonBody parameter p endsWith v
      }

    private def condition: Parser[Condition] =
      (ALWAYS | HEADER | QUERY | (JSON <~ BODY)) ^^ {
        case `ALWAYS` => Always()
        case `HEADER` => Header()
        case `QUERY`  => Query()
        case `JSON`   => JsonBody()
      }

    private def checkField: Parser[(String, String)] =
      (NAME | PARAMETER) ~ stringLit ^^ { case n ~ s => (n, s) }

    private def checkValue: Parser[Evaluator] =
      (IS | CONTAINS | (STARTS <~ WITH) | (ENDS <~ WITH)) ~ stringLit ^^ {
        case `IS`       ~ v => Is(v)
        case `CONTAINS` ~ v => Contains(v)
        case `STARTS`   ~ v => StartsWith(v)
        case `ENDS`     ~ v => EndsWith(v)
        case _              => True()
      }
    // format: ON

  }

  implicit class ConditionParser(s: String) {
    def parseCondition: Try[Condition] = Try(Parser.transform(s).get)
  }

}
