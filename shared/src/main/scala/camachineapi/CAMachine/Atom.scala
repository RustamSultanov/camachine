package camachineapi.CAMachine

class Atom(str: String) extends Element {

  private var value: String = str

  def getValue = value

  def setValue(str: String) =
    value = str

  override def toString(): String = super.toString()
}
