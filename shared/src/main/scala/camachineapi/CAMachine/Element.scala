package camachineapi.CAMachine

trait Element {
  override def toString: String =
    this match {
      case a: Atom => a.getValue
      case a: Pair => {
        var str: String = ""
        str = str + "("
        if (a.getFirst == null) str = str + "null"
        else str = str + a.getFirst.toString()
        str = str + ","
        if (a.getSecond == null) str = str + "null"
        else str = str + a.getSecond.toString()
        str = str + ")"
        str
      }
      case a: Function =>
        if (a.getElement == null) a.getFunc + ":" + "null"
        else a.getFunc + ":" + a.getElement.toString()
      case _ => ""
    }
}
