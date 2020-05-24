package camachineapi.config

import pureconfig.generic.auto._
import pureconfig.{ConfigReader, Derivation}

case class Http(port: Int, host: String)
case class AppConfig(http: Http, assets: String)

object AppConfig {
  implicit val appConfigReader = implicitly[Derivation[ConfigReader[AppConfig]]]
}
