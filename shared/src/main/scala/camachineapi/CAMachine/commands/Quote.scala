package camachineapi.CAMachine.commands

import camachineapi.CAMachine.{Atom, Code, Memory, Stack, Term}

class Quote(ch: String) extends Command {
  override def exec(
    term: Term,
    code: Code,
    stack: Stack,
    memory: Memory,
  ): Boolean = {
    if (ch == "") term.setValue(null)
    else term.setValue(new Atom(ch))
    code.removeHead
    true
  }

  def getQuote: String = ch
  override def toString: String = "quote(" + ch + ")"

}
