package camachineapi.LambdaToCCL

sealed trait TermCCL

case class CombCur(body: TermCCL) extends TermCCL
case class CombApp(termCCL1: TermCCL, termCCL2: TermCCL) extends TermCCL
case class Dpair(termCCL1: TermCCL, termCCL2: TermCCL) extends TermCCL

case class Fst(p1: Option[TermCCL] = None, p2: Option[TermCCL] = None)
    extends TermCCL {
  override def toString: String = "Fst"
}

case class Snd(p1: Option[TermCCL] = None, p2: Option[TermCCL] = None)
    extends TermCCL {
  override def toString: String = "Snd"
}

object DeBruijnToCcl {

  val fst = Fst()
  val snd = Snd()
//  App(Lambda(Var(Index(0,"a")),Var(Index(1,"b"))),Var(Index(1,"x")))
//  CombApp(CombCur(CombApp(Fst(None,None),Snd(None,None)),Snd(None,None)),CombApp(Fst(None,None),Snd(None,None)))
  def toCCL(term: Term): TermCCL = term match {
    case Var(name) =>
      name match {
        case Index(j, _) if j == 0 => snd
        case _                     => Dpair(fst, snd)
      }
    case Lambda(_, body)   => CombCur(toCCL(body))
    case App(term1, term2) => CombApp(toCCL(term1), toCCL(term2))

  }

}
