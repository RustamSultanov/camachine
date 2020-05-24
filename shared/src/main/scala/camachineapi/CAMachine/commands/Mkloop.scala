package camachineapi.CAMachine.commands
import camachineapi.CAMachine._
import camachineapi.CAMachine.{Atom, Code, Memory, Stack, Term}

class Mkloop extends Command {
  override def exec(
    term: Term,
    code: Code,
    stack: Stack,
    memory: Memory,
  ): Boolean = {
    term.getValue match {
      case function: Function =>
        val v = function.getFunc
        val rv = memory.addRecVar(v)
        term.setValue(new Atom(rv))
        code.removeHead
      case _ =>
        println(
          "mkloop - Терм не является абстракцией" + " Код:" + code.toString,
        )
        return false
    }
    return true
  }

  override def toString = "mkloop"
}
