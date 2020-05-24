package camachineapi.CAMachine

import camachineapi.CAMachine.commands._

class Code(com: List[Command]) {

  def setCode(com: List[Command]) =
    commands = com

  private var commands: List[Command] = com

  def getCommands = commands

  def getHead = commands.head

  def removeHead() =
    if (commands.tail.isEmpty) {
      commands = List()
    } else commands = commands.tail

  def addCommands(code: Code) =
    this.commands = code.getCommands ++ this.commands

  def addCommand(command: Command) = commands = List(command) ++ this.commands

  def this(s: String) = {
    this(List())
    var string = s
    commands = List();
    while (string.length > 0) {
      if (string.length >= 3) {
        val str: String = string.substring(0, 3)
        str match {
          case "pus" =>
            commands = commands :+ new Push()
            if (string.length > 4) string = string.substring(5)
            else string = ""
          case "swa" =>
            commands = commands :+ new Swap()
            if (string.length > 4) string = string.substring(5)
          case "con" =>
            commands = commands :+ new Cons()
            if (string.length > 4) string = string.substring(5)
            else string = ""
          case "cur" =>
            var j: Int = 4
            var k: Int = 1
            while (k > 0) {
              if (string.charAt(j) == '(') k = k + 1
              if (string.charAt(j) == ')') k = k - 1
              j = j + 1
            }
            commands = commands :+ new Cur(
              new Code(string.substring(4, j - 1)),
            )
            if (string.length > j) string = string.substring(j + 1)
            else string = ""
          case "quo" =>
            var j: Int = 6
            var k: Int = 1
            while (k > 0) {
              if (string.charAt(j) == '(') k = k + 1
              if (string.charAt(j) == ')') k = k - 1
              j = j + 1
            }
            commands = commands :+ new Quote(
              string.substring(6, j - 1),
            )
            if (string.length > j) string = string.substring(j + 1)
            else string = ""
          case "car" =>
            commands = commands :+ new Car()
            if (string.length > 3) string = string.substring(4)
            else string = ""
          case "cdr" =>
            commands = commands :+ new Cdr()
            if (string.length > 3) string = string.substring(4)
            else string = ""
          case "app" =>
            commands = commands :+ new App()
            if (string.length > 3) string = string.substring(4)
            else string = ""
          case "bra" =>
            var j: Int = 7
            var k: Int = 1
            while (k > 0) {
              if (string.charAt(j) == '(') k = k + 1
              if (string.charAt(j) == ')') k = k - 1
              j = j + 1
            }
            val i = j - 1
            j = j + 1
            k = 1
            while (k > 0) {
              if (string.charAt(j) == '(') k = k + 1
              if (string.charAt(j) == ')') k = k - 1
              j = j + 1
            }
            commands = commands :+ new Branch(
              new Code(string.substring(7, i)),
              new Code(string.substring(i + 2, j - 1)),
            )
            if (string.length > j) string = string.substring(j + 1)
            else string = ""
          case "add" =>
            commands = commands :+ new Add()
            if (string.length > 3) string = string.substring(4)
            else string = ""
          case "equ" =>
            commands = commands :+ new Equal()
            if (string.length > 5) string = string.substring(6)
            else string = ""
          case "mul" =>
            commands = commands :+ new Mult()
            if (string.length > 4) string = string.substring(5)
            else string = ""
          case "rec" =>
            var j: Int = 6
            var k: Int = 1
            while (k > 0) {
              if (string.charAt(j) == '(') k = k + 1
              if (string.charAt(j) == ')') k = k - 1
              j = j + 1
            }
            commands = commands :+ new Recur(
              new Code(string.substring(6, j - 1)),
            )
            if (string.length > j) string = string.substring(j + 1)
            else string = ""
          case "min" =>
            commands = commands :+ new Minus()
            if (string.length > 5) string = string.substring(6)
            else string = ""
          case "mkl" =>
            commands = commands :+ new Mkloop()
            if (string.length > 6) string = string.substring(7)
            else string = ""
          case "fre" =>
            commands = commands :+ new Freeze()
            if (string.length > 6) string = string.substring(7)
            else string = ""
          case "unf" =>
            commands = commands :+ new Unfreeze()
            if (string.length > 8) string = string.substring(9)
            else string = ""
          case _ => string = ""
        }
      } else string = ""
    }
  }

  override def toString: String = {
    var str: String = ""
    for (i <- commands) {
      str += i.toString + " "
    }
    if (!(str == "")) str = str.substring(0, str.length - 1)
    str
  }
}
