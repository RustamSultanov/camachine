package camachineapi.LambdaToCCL

import org.specs2.Specification
import LambdaToCCL.SimpleLambdaParser._

final class LambdaParserSpec extends Specification {
  def is =
    s2"""
      LambdaParser should
        $parse_lambda
        $parse_app
        $parse_complex_expr
    """
  private def parse_lambda =
    parse("\\s.a\n") mustEqual Lambda(Var(Literal("s")), Var(Literal("a")))

  private def parse_app =
    parse("(s a)\n") mustEqual App(Var(Literal("s")), Var(Literal("a")))

  private def parse_complex_expr =
    parse("\\x.(xx)a\n") mustEqual App(Lambda(Var(Literal("x")),App(Var(Literal("x")),Var(Literal("x")))),Var(Literal("a")))

}