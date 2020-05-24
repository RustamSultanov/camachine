package camachineapi.CAMachine.commands
import camachineapi.CAMachine.{Code, Memory, Stack, Term, _}

class Cons extends Command {
  override def exec(
    term: Term,
    code: Code,
    stack: Stack,
    memory: Memory,
  ): Boolean = {
    term.setValue(new Pair(stack.getValue, term.getValue))
    stack.removeElement()
    code.removeHead()
    true
  }

  override def toString: String = "cons"
}
