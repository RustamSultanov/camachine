package camachineapi.models

import camachineapi.CAMachine._
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

final case class CamCompilerResult(
  recvars: List[RecvarsDto],
  variables: List[VariableDto],
  rows: List[Row],
  error: String,
)
final case class RecvarsDto(name: String, value: String)

object RecvarsDto {
  implicit val recvarsDtoEncoder: Encoder[RecvarsDto] =
    deriveEncoder[RecvarsDto]
  implicit val recvarsDtoDecoder: Decoder[RecvarsDto] =
    deriveDecoder[RecvarsDto]
}

final case class VariableDto(name: String, value: String)

object VariableDto {
  implicit val variableEncoder: Encoder[VariableDto] =
    deriveEncoder[VariableDto]
  implicit val variableDecoder: Decoder[VariableDto] =
    deriveDecoder[VariableDto]
}
object CamCompilerResult {
  implicit val camCompilerResultEncoder: Encoder[CamCompilerResult] =
    deriveEncoder[CamCompilerResult]
  implicit val camCompilerResultDecoder: Decoder[CamCompilerResult] =
    deriveDecoder[CamCompilerResult]
}
