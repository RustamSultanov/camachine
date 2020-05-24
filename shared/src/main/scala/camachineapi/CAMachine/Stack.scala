package camachineapi.CAMachine

class Stack(elem: Element) {
  private var value: Element = elem
  private var next: Stack = _
  def setValue(elem: Element) =
    value = elem
  def getValue = value
  def setNext(nxt: Stack) =
    next = nxt
  def getNext = next

  def removeElement() = {
    this.setValue(this.getNext.getValue)
    this.setNext(this.getNext.getNext)
  }

  def addElement(elem: Element) = {
    val temp: Stack = new Stack(this.getValue)
    temp.setNext(this.getNext)
    this.setValue(elem)
    this.setNext(temp)
  }

  override def toString: String = {
    var str: String = ""
    if (this.getValue == null) str = str + "null"
    else str = str + this.getValue.toString
    if (this.getNext != null) str = str + "; " + this.getNext.toString()
    else str = str + "."
    str
  }
}
