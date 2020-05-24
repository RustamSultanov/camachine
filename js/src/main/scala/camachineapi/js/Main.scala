package camachineapi.js

import camachineapi.js.http.Client
import cats.effect.{ExitCode, IO, IOApp}
import outwatch.dom._
import outwatch.dom.dsl._
import monix.execution.Scheduler.Implicits.global
import org.scalajs.dom.document

object Main extends IOApp {
  private lazy val client =
    if (document.location.protocol.startsWith("file"))
      new Client("http://localhost:8080")
    else
      new Client("")

  def run(args: List[String]): IO[ExitCode] =
    for {
      topComponent <- TopComponent.init(client)
      _ <- OutWatch.renderReplace(
        "#main",
        div(topComponent.node, styles.height := "100%"),
      )
    } yield ExitCode.Success
}
