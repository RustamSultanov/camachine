package camachineapi.LambdaToCCL

import LambdaToCCL.DeBruijnToCcl._
import org.specs2.Specification

final class DeBruijnToCclSpec extends Specification {
  def is =
    s2"""
      DeBruijnToCcl should
        $convert_to_CCL_term
    """
  private def convert_to_CCL_term =
    toCCL(App(Lambda(Var(Literal("x")),Var(Index(0,"x"))),Lambda(Var(Literal("x")),Var(Index(0,"x"))))) must beEqualTo(
      CombApp(CombCur(snd),CombCur(snd))
    )
}
