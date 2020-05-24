package camachineapi.CAMachine

class Term(elem: Element) {

  private var value: Element = elem

  def getValue = value

  def setValue(elem: Element) =
    value = elem

  override def toString: String =
    if (this.getValue == null) "null"
    else this.getValue.toString()
}
