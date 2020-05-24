package camachineapi.CAMachine

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

class Memory {
  private var error: String = "Ошибок нет"
  def getError = error
  def setError(str: String) =
    error = str

  var recvars: Array[RecVariable] = Array[RecVariable]()
  def addRecVar(v: String): String = {
    val n: String = "rv" + recvars.length
    recvars = recvars ++ Array(
      new RecVariable(n, new Function(v, new Pair(null, new Atom(n)))),
    )
    n
  }

  def findRecVar(n: String): Function = {
    var i: Int = 0
    while (i < recvars.length) {
      if (recvars(i).getName.equals(n)) return recvars(i).getValue
      i += 1
    }
    null
  }

  var variables: Array[Variable] = Array[Variable]()
  def addVariable(v: Code): String = {
    val n: String = "v" + variables.length
    variables = variables ++ Array(Variable(n, v))
    n
  }
  def findValue(n: String): Code = variables(n.substring(1).toInt).getValue

  private var rows: Array[Row] = Array[Row]()
  def addRow(term: String, code: String, stack: String) =
    rows = rows ++ Array(Row(term, code, stack))
  def getRows = rows
}

case class RecVariable(name: String, value: Function) {
  def getName = name
  def getValue = value
}

case class Row(term: String, code: String, stack: String)

object Row {
  implicit val rowEncoder: Encoder[Row] =
    deriveEncoder[Row]
  implicit val rowDecoder: Decoder[Row] =
    deriveDecoder[Row]
}

case class Variable(name: String, value: Code) {
  def getName = name
  def getValue = value
}
