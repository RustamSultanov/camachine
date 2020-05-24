package camachineapi.CAMachine.commands
import camachineapi.CAMachine.{Code, Memory, Stack, Term, _}

class Cur(c: Code) extends Command {
  override def exec(
    term: Term,
    code: Code,
    stack: Stack,
    memory: Memory,
  ): Boolean = {
    val v: String = memory.addVariable(c)
    term.setValue(new Function(v, term.getValue))
    code.removeHead()
    true
  }

  def getCode: Code = c

  override def toString: String = "cur(" + c.toString + ")"
}
