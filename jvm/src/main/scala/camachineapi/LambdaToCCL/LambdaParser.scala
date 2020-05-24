package camachineapi.LambdaToCCL

import scala.util.parsing.combinator.lexical.StdLexical
import scala.util.parsing.combinator.{PackratParsers, RegexParsers}
import scala.util.parsing.input.CharSequenceReader

class LambdaParser extends RegexParsers with PackratParsers {
  type Tokens = StdLexical
  val lexical = new CustomLambdaLexer
  lexical.delimiters ++= Seq("\\", ".", "(", ")")

  lazy val lambdaExpression
    : PackratParser[Term] = application | otherExpressionTypes
  lazy val otherExpressionTypes: Parser[Term] = variable | parens | lambda
  lazy val lambda: PackratParser[Lambda] =
    positioned(("\\") ~> variable ~ "." ~ parens ^^ {
      case arg ~ "." ~ body => {
        Lambda(arg, body)
      }
    })
  lazy val application: PackratParser[App] =
    positioned(lambdaExpression ~ otherExpressionTypes ^^ {
      case left ~ right => {
        App(left, right)
      }
    })
  lazy val variable: PackratParser[Var] =
    positioned("""[A-Za-z]""".r ^^ ((x: String) => {
      Var(Literal(x.charAt(0).toString))
    }))
  lazy val parens
    : PackratParser[Term] = "(" ~> lambdaExpression <~ ")" | lambdaExpression
}

class CustomLambdaLexer extends StdLexical {
  override def letter: Parser[Char] =
    elem(
      "letter",
      c =>
        c.isLetter &&
          c != '(' && c != ')' && c != '\\' && c != '.',
    )
}

object SimpleLambdaParser extends LambdaParser {
  def parse(s: CharSequence): Term =
    parse(new CharSequenceReader(s))

  def parse(input: CharSequenceReader): Term =
    parsePhrase(input) match {
      case Success(t, _) => t
      case NoSuccess(msg, next) =>
        throw new IllegalArgumentException(
          "Could not parse '" + input + "' near '" + next.pos.longString + ": " + msg,
        )
    }

  def parsePhrase(input: CharSequenceReader): ParseResult[Term] =
    phrase(lambdaExpression)(input)
}
