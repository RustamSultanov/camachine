package camachineapi.models

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

final case class CamCompilerData(
  optimisation: Int,
  commands: String,
)

object CamCompilerData {
  implicit val camCompilerDataEncoder: Encoder[CamCompilerData] = deriveEncoder
  implicit val camCompilerDataDecoder: Decoder[CamCompilerData] = deriveDecoder
}
