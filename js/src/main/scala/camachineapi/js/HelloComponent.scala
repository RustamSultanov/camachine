package camachineapi.js

import camachineapi.models.Hello
import cats.effect.IO
import io.circe.parser._
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable
import org.scalajs.dom.document
import outwatch.dom._
import outwatch.dom.dsl._
import outwatch.http.Http

final private class HelloComponent {

  private lazy val protocolHost =
    if (document.location.protocol.startsWith("file"))
      "http://localhost:8080"
    else
      ""

  private def requests(
    urls: Observable[String],
  ): Observable[Hello] =
    for {
      response <- Http.getWithUrl(urls)
      result <- Observable.fromEither(
        parse(response.xhr.responseText).flatMap(_.as[Hello]),
      )
    } yield result

  val node: IO[VNode] =
    for {
      submits    <- Handler.create[String]("")
      textValues <- Handler.create[String]
      urls = for {
        value <- submits
      } yield s"$protocolHost/hello?value=$value"
      hellos = requests(urls)
      messages = hellos.map(
        (hello: Hello) =>
          s"'${hello.value}' was said at ${hello.epochSecond} epoch second",
      )
    } yield
      div(
        cls := "container",
        marginTop := "50px",
        div(
          cls := "form-row align-items-center",
          div(
            cls := "col-auto",
            label(cls := "sr-only", `for` := "searchInputId", "Some Text"),
            input(
              `type` := "text",
              cls := "form-control mb-2",
              id := "searchInputId",
              placeholder := "Some Text",
              onInput.value --> textValues,
            ),
          ),
          div(
            cls := "col-auto",
            button(
              "Send",
              `type` := "submit",
              cls := "btn btn-primary mb-2",
              onClick(textValues) --> submits,
            ),
          ),
        ),
        div("Result:"),
        div(messages),
      )

}
