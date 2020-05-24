package camachineapi.LambdaToCCL

import org.specs2.Specification
import LambdaToCCL.LambdaReduce._

final class LambdaToDeBruijnSpec extends Specification {
  def is =
    s2"""
      LambdaReduce should
        $substitude_beta_reduction
    """
  private def substitude_beta_reduction =
    beta(App(Lambda(Var(Index(0,"x")),Var(Literal("x"))),Lambda(Var(Index(0,"x")),Var(Literal("x"))))) mustEqual
      Some(Var(Literal("x")))

}
