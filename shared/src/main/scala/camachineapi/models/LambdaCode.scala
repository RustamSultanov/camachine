package camachineapi.models

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class LambdaCode(code: String) extends AnyVal

object LambdaCode {
  implicit val lambdaCodeEncoder: Encoder[LambdaCode] = deriveEncoder
  implicit val lambdaCodeDecoder: Decoder[LambdaCode] = deriveDecoder
}
