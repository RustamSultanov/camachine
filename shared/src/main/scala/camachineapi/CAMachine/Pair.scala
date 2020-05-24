package camachineapi.CAMachine

class Pair(fst: Element, snd: Element) extends Element {

  private var first: Element = fst

  private var second: Element = snd

  def getFirst = first

  def getSecond = second

  def setFirst(fst: Element) =
    first = fst

  def setSecond(snd: Element) =
    second = snd

  override def toString(): String = super.toString()
}
