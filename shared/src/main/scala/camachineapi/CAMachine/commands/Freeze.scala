package camachineapi.CAMachine.commands
import camachineapi.CAMachine.{Code, Memory, Stack, Term, _}

class Freeze extends Command {
  override def exec(
    term: Term,
    code: Code,
    stack: Stack,
    memory: Memory,
  ): Boolean = {
    code.removeHead()
    val v = memory.addVariable(new Code(List(code.getHead)))
    term.setValue(new Function(v, term.getValue))
    code.removeHead()
    true
  }

  override def toString: String = "freeze"
}
