package camachineapi

import camachineapi.config.{AppConfig, SyncConfig}
import camachineapi.http.CamApiDocs.api
import camachineapi.http._
import cats.effect._
import cats.implicits._
import endpoints.openapi.model.OpenApi
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.CORS
import pureconfig.ConfigSource
import scribe.{Level, Logger}
import sttp.tapir.swagger.http4s.SwaggerHttp4s

import scala.concurrent.ExecutionContext

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO(
        Logger.root
          .clearHandlers().clearModifiers()
          .withHandler(minimumLevel = Some(Level.Debug))
          .replace(),
      )
      appConfig <- SyncConfig.read[IO, AppConfig](ConfigSource.default)
      exitCode  <- app(appConfig).use(_ => IO.never).as(ExitCode.Success)
    } yield exitCode

  private def app(appConfig: AppConfig): Resource[IO, Server[IO]] =
    for {
      blocker <- Blocker[IO]
      staticEndpoints = new StaticEndpoints[IO](appConfig.assets, blocker)
      docRoutes = new SwaggerHttp4s(
        OpenApi.stringEncoder.encode(api),
        "docs",
        "docs.json",
      ).routes[IO]
      httpApp = (
        staticEndpoints.endpoints() <+> CamEndpoints
          .endpoints() <+> LambdaToCclEndpoints.endpoints() <+> docRoutes
      ).orNotFound
      server <- BlazeServerBuilder[IO](ExecutionContext.global)
        .bindHttp(appConfig.http.port, appConfig.http.host)
        .withHttpApp(CORS(httpApp))
        .resource
    } yield server
}
