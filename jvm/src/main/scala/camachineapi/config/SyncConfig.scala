package camachineapi.config

import cats.effect.Sync
import cats.implicits._
import pureconfig.{ConfigReader, ConfigSource, Derivation}
import pureconfig.error.ConfigReaderFailures

class SyncConfigException(failures: ConfigReaderFailures)
    extends RuntimeException(failures.toList.mkString(" "))

object SyncConfig {
  def read[F[_]: Sync, A](
    configSource: ConfigSource,
  )(implicit reader: Derivation[ConfigReader[A]]): F[A] =
    Sync[F].delay(configSource.load[A]).flatMap {
      case Right(conf) => Sync[F].pure(conf)
      case Left(e)     => Sync[F].raiseError(new SyncConfigException(e))
    }
}
