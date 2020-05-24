package camachineapi.CAMachine

class Function(str: String, elem: Element) extends Element {

  private var func: String = str

  private var element: Element = elem

  def getFunc = func

  def setFunc(str: String) =
    func = str

  def getElement = element

  def setElement(elem: Element) =
    element = elem

  override def toString(): String = super.toString()
}
