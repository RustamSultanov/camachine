package camachineapi.CAMachine

import camachineapi.CAMachine.commands._

class Compiler {
  val stack: Stack = new Stack(null)
  var code: Code = new Code(List())
  val term: Term = new Term(null)
  val memory: Memory = new Memory()
  private var noErrors: Boolean = true

  def makeStep(term: Term, code: Code, stack: Stack, memory: Memory): Boolean =
    code.getHead.exec(term, code, stack, memory)

  def betaFolding(code: Code): Unit = {
    var c = code.getCommands
    if (c.head.isInstanceOf[Push]) {
      var newCode = List(c.head)
      c = c.tail
      c.head match {
        case cur: Cur =>
          val x = cur.getCode.getCommands
          c = c.tail
          if (c.head.isInstanceOf[Swap]) {
            c = c.tail
            var i = 1
            while (i > 0) {
              if (c.head.isInstanceOf[Push]) i = i + 1
              if (c.head.isInstanceOf[Cons]) i = i - 1
              newCode = newCode :+ c.head
              c = c.tail
            }
            newCode = List.concat(newCode, x)
            if (c.head.isInstanceOf[App]) {
              newCode = List.concat(newCode, c.tail)
              code.setCode(newCode)
              memory.addRow("-", "Оптимизация Бета-свертывание", "-")
              memory.addRow("-", code.toString, "-")
            }
          }
        case _ =>
      }
    }
  }

  def doublingFunctions(code: Code): Unit = {
    var c = code.getCommands
    if (c.head.isInstanceOf[Push]) {
      var newCode = List(c.head)
      c = c.tail
      if (c.head.isInstanceOf[Push]) {
        c = c.tail
        c.head match {
          case quote: Quote =>
            val x = quote.getQuote
            if (x.matches("[+-=*/]")) {
              c = c.tail
              if (c.head.isInstanceOf[Swap]) {
                var i = 1
                c = c.tail
                while (i > 0) {
                  newCode = newCode :+ c.head
                  c = c.tail
                  if (c.head.isInstanceOf[Push]) i = i + 1
                  if (c.head.isInstanceOf[Cons]) i = i - 1
                }
                c = c.tail.tail
                i = 1
                while (i > 0) {
                  if (c.head.isInstanceOf[Push]) i = i + 1
                  if (c.head.isInstanceOf[Cons]) i = i - 1
                  newCode = newCode :+ c.head
                  c = c.tail
                }
                x match {
                  case "+" => newCode = newCode :+ new Add
                  case "-" => newCode = newCode :+ new Minus
                  case "*" => newCode = newCode :+ new Mult
                  case "/" => newCode = newCode :+ new Mod
                  case "=" => newCode = newCode :+ new Equal
                }
                if (c.head.isInstanceOf[App]) {
                  newCode = List.concat(newCode, c.tail)
                  code.setCode(newCode)
                  memory.addRow("-", "Оптимизация Двухместные функции", "-")
                  memory.addRow("-", code.toString, "-")
                }

              }
            }
          case _ =>
        }
      }
    }
  }

  def optimize(code: Code, optimisation: Int) =
    optimisation match {
      case 0 =>
      case 1 => betaFolding(code)
      case 2 => doublingFunctions(code)
      case 3 =>
        betaFolding(code)
        doublingFunctions(code)
    }

  def compile(input: String, optimisation: Int) = {
    code = new Code(input)
    memory.addRow(term.toString(), code.toString, stack.toString())
    var i = 0
    while (code.getCommands.nonEmpty && (noErrors) && (i < 1000)) {
      optimize(code, optimisation)
      noErrors = makeStep(term, code, stack, memory)
      if (noErrors)
        memory.addRow(term.toString(), code.toString, stack.toString())
      i += 1
    }
  }

}
