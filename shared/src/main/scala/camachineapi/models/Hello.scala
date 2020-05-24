package camachineapi.models

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

final case class Hello(
  epochSecond: Long,
  value: String,
)

object Hello {
  implicit val helloEncoder: Encoder[Hello] = deriveEncoder
  implicit val helloDecoder: Decoder[Hello] = deriveDecoder
}
