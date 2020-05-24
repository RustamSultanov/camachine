package camachineapi.CAMachine.commands
import camachineapi.CAMachine._
import camachineapi.CAMachine.{Atom, Code, Memory, Stack, Term}

class Unfreeze extends Command {
  override def exec(
    term: Term,
    code: Code,
    stack: Stack,
    memory: Memory,
  ): Boolean = {
    term.getValue match {
      case atom: Atom =>
        val rv: Function = memory.findRecVar(atom.getValue)
        if (rv != null) {
          term.setValue(rv)
        }
      case _ =>
    }
    term.getValue match {
      case function: Function =>
        code.removeHead()
        code.addCommands(memory.findValue(function.getFunc))
        term.setValue(function.getElement)
      case _ => code.removeHead()
    }
    true
  }

  override def toString: String = "unfreeze"
}
