package camachineapi.CAMachine.commands
import camachineapi.CAMachine.{Code, Memory, Stack, Term}

trait Command {
  def exec(term: Term, code: Code, stack: Stack, memory: Memory): Boolean
}
