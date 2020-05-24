package camachineapi.CAMachine.commands

import camachineapi.CAMachine._
import camachineapi.CAMachine.{Atom, Code, Memory, Stack, Term}

class App extends Command {
  override def exec(
    term: Term,
    code: Code,
    stack: Stack,
    memory: Memory,
  ): Boolean = {
    term.getValue match {
      case pair: Pair =>
        pair.getFirst match {
          case atom1: Atom =>
            val atom = atom1.getValue
            if (atom.matches("[+-=*/]")) {
              code.removeHead
              term.setValue(
                new Atom(atom + pair.getSecond.asInstanceOf[Atom].getValue),
              )
              return true
            }
            if (atom.charAt(0).toString.matches("[+-=*/]")) {
              code.removeHead
              val ch = atom.charAt(0).toString
              atom1.setValue(atom.substring(1))
              ch match {
                case "+" => code.addCommand(new Add)
                case "-" => code.addCommand(new Minus)
                case "=" => code.addCommand(new Equal)
                case "*" => code.addCommand(new Mult)
                case "/" => code.addCommand(new Mod)
                case _   => println(ch)
              }
              return true
            }
            val rv: Function = memory.findRecVar(atom1.getValue)
            if (rv != null) {
              pair.setFirst(rv)
            }
          case _ =>
        }
        pair.getFirst match {
          case function: Function =>
            code.removeHead
            code.addCommands(memory.findValue(function.getFunc))
            term.setValue(new Pair(function.getElement, pair.getSecond))
          case _ =>
            memory.setError(
              "app - Первый элемент пары не является функцией" + " Код:" + code.toString,
            )
            return false
        }
      case _ =>
        memory.setError(
          "app - Терм не является парой" + " Код:" + code.toString,
        )
        return false
    }
    true
  }

  override def toString: String = "app"
}
