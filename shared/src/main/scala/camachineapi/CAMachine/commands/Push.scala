package camachineapi.CAMachine.commands

import camachineapi.CAMachine.{Code, Memory, Stack, Term}

class Push extends Command {
  override def exec(
    term: Term,
    code: Code,
    stack: Stack,
    memory: Memory,
  ): Boolean = {
    stack.addElement(term.getValue)
    code.removeHead()
    true
  }

  override def toString: String = "push"
}
