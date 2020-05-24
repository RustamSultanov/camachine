package camachineapi.LambdaToCCL

import scala.util.parsing.input.Positional

sealed trait Term extends Positional

sealed trait Name

case class Var(name: Name) extends Term
case class Lambda(param: Var, body: Term) extends Term
case class App(term1: Term, term2: Term) extends Term
case class Index(i: Int, name: String) extends Name
case class Literal(name: String) extends Name

object LambdaReduce {

  def substitute(term: Term, i: Int, body: Term): Term = body match {
    case Var(name) =>
      name match {
        case Index(j, _) if i == j => term
        case _                     => Var(name)
      }
    case Lambda(param, body) => Lambda(param, substitute(term, i + 1, body))
    case App(term1, term2) =>
      App(substitute(term, i, term1), substitute(term, i, term2))
    case _ => body
  }

  val beta: Term => Option[Term] = {
    case App(Lambda(Var(Index(i, name)), body), term) =>
      Some(substitute(term, i, body))
    case _ =>
      None
  }

  def toNormalForm(step: Term => Option[Term]): Term => Term =
    (term: Term) => step(term).fold(term)(toNormalForm(step))

//  val b = beta(App(Lambda(Var(Index(0,"a")),Var(Index(1,"x"))),Var(Index(1,"x"))))
//  val nf = toNormalForm(beta)
//  print(nf)
  def mu(reduction: Term => Option[Term]): Term => Option[Term] = {
    case App(term, params) => reduction(term).map(App(_, params))
    case _                 => None
  }

  def nu(reduction: Term => Option[Term]): Term => Option[Term] = {
    case App(term, param) => reduction(param).map(App(term, _))
    case _                => None
  }

  def deepMu(reduction: Term => Option[Term]): Term => Option[Term] = {
    case App(term, params) => mu(reduction)(term).map(App(_, params))
    case other             => reduction(other)
  }

  def deepNu(reduction: Term => Option[Term]): Term => Option[Term] = {
    case App(term, param) => nu(reduction)(param).map(App(term, _))
    case other            => reduction(other)
  }

}
//val munu = mu(nu(beta))
//val mununf = toNormalForm(munu)
