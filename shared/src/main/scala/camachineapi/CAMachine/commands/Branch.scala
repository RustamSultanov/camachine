package camachineapi.CAMachine.commands
import camachineapi.CAMachine._

class Branch(c1: Code, c2: Code) extends Command {
  override def exec(
    term: Term,
    code: Code,
    stack: Stack,
    memory: Memory,
  ): Boolean = {
    code.removeHead
    term.getValue match {
      case atom: Atom =>
        if (atom.getValue.equals("true")) {
          code.addCommands(c1)
        } else {
          if (atom.getValue.equals("false")) {
            code.addCommands(c2)
          } else {
            memory.setError(
              "branch - Значение атома не является булевым" + " Код:" + code.toString,
            )
            return false
          }
        }
      case _ =>
        memory.setError(
          "branch - Терм не является атомом" + " Код:" + code.toString,
        )
        return false
    }
    term.setValue(stack.getValue)
    stack.removeElement
    true
  }

  override def toString: String =
    "branch(" + c1.toString + ")(" + c2.toString + ")"
}
