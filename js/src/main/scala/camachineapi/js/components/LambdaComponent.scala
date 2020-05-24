package camachineapi.js.components

import camachineapi.js.http.Client
import camachineapi.js.forms.Forms._
import camachineapi.models.LambdaCode
import cats.effect.{ContextShift, IO}
import monix.reactive.Observable
import outwatch.Handler
import outwatch.dom.VDomModifier
import outwatch.dom.dsl._

class LambdaComponent(
  textAreaLambda: Handler[String],
  resultLambda: Handler[LambdaCode],
  client: Client,
)(
  implicit
  contextShift: ContextShift[IO],
) {
  def dataStream: Observable[LambdaCode] = textAreaLambda.map(LambdaCode.apply)

  def sendRequestIO(data: LambdaCode): IO[LambdaCode] =
    client.requestLambda(data)

  def disableStream: Observable[Boolean] = dataStream.map(_.code.length < 4)

  def node = div(
    textAreaGroupRow(
      textAreaLambda,
      "lambdaTextArea",
      "Лямбда код",
      isRequired = true,
    ),
    buttonSubmitGroupRow(
      "Отправить",
      resultLambda,
      disableStream,
      dataStream,
      sendRequestIO,
    ),
    h3("Терм ККЛ полученый из лямбда-термов"),
    table(
      tr(
        tr(
          styleAttr := "bgcolor=#b9e4c9",
          VDomModifier(resultLambda.map(_.code)),
        ),
      ),
    ),
  )
}

object LambdaComponent {
  def init(
    client: Client,
  )(
    implicit
    contextShift: ContextShift[IO],
  ): IO[LambdaComponent] =
    for {
      textAreaLambdaHandler <- Handler.create[String]
      resultLambdaHandler   <- Handler.create[LambdaCode]
    } yield
      new LambdaComponent(
        textAreaLambdaHandler,
        resultLambdaHandler,
        client,
      )
}
