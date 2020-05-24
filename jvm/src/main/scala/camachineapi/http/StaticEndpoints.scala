package camachineapi.http

import cats.effect.{Blocker, ContextShift, Effect}
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, Request, StaticFile}

final class StaticEndpoints[F[_]: ContextShift: Effect](
  assetsPath: String,
  blocker: Blocker,
) extends Http4sDsl[F] {

  private[this] def static(
    file: String,
    request: Request[F],
  ) =
    StaticFile
      .fromResource("/" + file, blocker, Some(request))
      .getOrElseF(NotFound())

  private[this] def staticAssets(
    file: String,
    request: Request[F],
  ) =
    StaticFile
      .fromString(s"$assetsPath/$file", blocker, Some(request))
      .getOrElseF(NotFound())

  def endpoints(): HttpRoutes[F] = HttpRoutes.of[F] {
    case request @ GET -> Root =>
      static("index.html", request)
    case request @ GET -> Root / path
        if List(".js", ".css", ".map", ".html", ".ico").exists(path.endsWith) =>
      static(path, request)
    case request @ GET -> "front-res" /: path =>
      val fullPath = "front-res/" + path.toList.mkString("/")
      static(fullPath, request)
    case request @ GET -> "assets" /: path =>
      val fullPath = path.toList.mkString("/")
      staticAssets(fullPath, request)
  }

}
