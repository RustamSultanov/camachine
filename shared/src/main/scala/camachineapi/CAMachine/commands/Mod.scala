package camachineapi.CAMachine.commands
import camachineapi.CAMachine._
import camachineapi.CAMachine.{Atom, Code, Memory, Stack, Term}

class Mod extends Command {
  override def exec(
    term: Term,
    code: Code,
    stack: Stack,
    memory: Memory,
  ): Boolean = {
    term.getValue match {
      case pair: Pair =>
        pair.getFirst match {
          case atom: Atom if pair.getSecond.isInstanceOf[Atom] =>
            term.setValue(
              new Atom(
                (atom.getValue.toInt / pair.getSecond
                  .asInstanceOf[Atom].getValue.toInt).toString,
              ),
            )
          case _ =>
            memory.setError(
              "mod - Элементы пары не являются атомами" + " Код:" + code.toString,
            )
            return false
        }
      case _ =>
        memory.setError(
          "mod - Терм не является парой" + " Код:" + code.toString,
        )
        return false
    }
    code.removeHead
    true
  }

  override def toString: String = "mod"
}
