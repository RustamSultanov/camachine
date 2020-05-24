package camachineapi.CAMachine.commands
import camachineapi.CAMachine.{Code, Memory, Stack, Term, _}

class Swap extends Command {
  override def exec(
    term: Term,
    code: Code,
    stack: Stack,
    memory: Memory,
  ): Boolean = {
    val temp: Element = stack.getValue
    stack.setValue(term.getValue)
    term.setValue(temp)
    code.removeHead()
    true
  }

  override def toString: String = "swap"
}
