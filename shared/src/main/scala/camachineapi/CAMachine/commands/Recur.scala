package camachineapi.CAMachine.commands

import camachineapi.CAMachine.{Atom, Code, Memory, Stack, Term}

class Recur(c: Code) extends Command {
  override def exec(
    term: Term,
    code: Code,
    stack: Stack,
    memory: Memory,
  ): Boolean = {
    val v: String = memory.addVariable(c)
    val rv: String = memory.addRecVar(v)
    term.setValue(new Atom(rv))
    code.removeHead()
    true
  }

  override def toString: String = "recur(" + c.toString + ")"
}
