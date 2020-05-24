package camachineapi.CAMachine.commands
import camachineapi.CAMachine.{Code, Memory, Stack, Term, _}

class Car extends Command {
  override def exec(
    term: Term,
    code: Code,
    stack: Stack,
    memory: Memory,
  ): Boolean = {
    term.getValue match {
      case pair: Pair =>
        term.setValue(pair.getFirst)
      case _ =>
        memory.setError(
          "car - Терм не является парой" + " Код:" + code.toString,
        )
        return false
    }
    code.removeHead
    true
  }

  override def toString: String = "car"
}
