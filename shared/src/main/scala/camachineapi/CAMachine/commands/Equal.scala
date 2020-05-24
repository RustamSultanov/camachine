package camachineapi.CAMachine.commands
import camachineapi.CAMachine._
import camachineapi.CAMachine.{Atom, Code, Memory, Stack, Term}

class Equal extends Command {
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
            if (atom.getValue.equals(
                  pair.getSecond.asInstanceOf[Atom].getValue,
                )) {
              term.setValue(new Atom("true"))
            } else term.setValue(new Atom("false"))
          case _ =>
            memory.setError(
              "equal - Элементы пары не являются атомами" + " Код:" + code.toString,
            )
            return false
        }
      case _ =>
        memory.setError(
          "equal - Терм не является парой" + " Код:" + code.toString,
        )
        return false
    }
    code.removeHead
    true
  }

  override def toString: String = "equal"
}
