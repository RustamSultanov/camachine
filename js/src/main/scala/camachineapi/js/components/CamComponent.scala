package camachineapi.js.components

import camachineapi.js.http.Client
import camachineapi.js.forms.Forms._
import camachineapi.models.{CamCompilerData, CamCompilerResult}
import cats.effect.{ContextShift, IO}
import monix.reactive.Observable
import outwatch.Handler
import outwatch.dom.VDomModifier
import outwatch.dom.dsl._

class CamComponent(
  textAreaCam: Handler[String],
  radioInputCam: Handler[String],
  resultCam: Handler[CamCompilerResult],
  client: Client,
)(
  implicit
  contextShift: ContextShift[IO],
) {
  def choice(
    answer: Handler[String],
    question: String,
    options: List[String],
  ) =
    options.map(
      option =>
        radioGroupRow(answer, "opt" + option, option, isRequired = true),
    )

  def convertRadioToInt(value: String) = value match {
    case "Без оптимизации"                        => 0
    case "Бета-свертывание"                       => 1
    case "Двухместные функции"                    => 2
    case "Бета-свертывание + Двухместные функции" => 3
    case _                                        => 0
  }
  def dataStream: Observable[CamCompilerData] =
    Observable
      .combineLatestMap2(radioInputCam, textAreaCam)(
        (r, t) => CamCompilerData.apply(convertRadioToInt(r), t),
      )

  def sendRequestIO(data: CamCompilerData): IO[CamCompilerResult] =
    client.requestCam(data)

  def disableStream: Observable[Boolean] = dataStream.map(_.commands.length < 4)

  def node = div(
    div(
      cls := "form-group",
      p(
        choice(
          radioInputCam,
          "Оптимизация",
          List(
            "Без оптимизации",
            "Бета-свертывание",
            "Двухместные функции",
            "Бета-свертывание + Двухместные функции",
          ),
        ),
      ),
    ),
    textAreaGroupRow(
      textAreaCam,
      "camTextArea",
      "КАМ код",
      "Введите последовательность команд КАМ через пробел",
      isRequired = true,
    ),
    buttonSubmitGroupRow(
      "Отправить",
      resultCam,
      disableStream,
      dataStream,
      sendRequestIO,
    ),
    h3("Состояния КАМ на каждом шагу выполнения"),
    table(
      VDomModifier(
        resultCam.map(
          _.recvars.map(
            recvar =>
              tr(
                td(
                  recvar.name,
                ),
                td(
                  recvar.name,
                ),
              ),
          ),
        ),
      ),
      VDomModifier(
        resultCam.map(
          _.variables.map(
            variable =>
              tr(
                td(
                  variable.name,
                ),
                td(
                  variable.name,
                ),
              ),
          ),
        ),
      ),
      VDomModifier(
        resultCam.map(
          _.rows.map(
            row =>
              tr(
                td(
                  styleAttr := "bgcolor=#b9e4c9",
                  row.term,
                ),
                td(
                  styleAttr := "bgcolor=#fedbd0",
                  row.code,
                ),
                td(
                  styleAttr := "bgcolor=#b9e4c9",
                  row.stack,
                ),
              ),
          ),
        ),
      ),
    ),
  )
}

object CamComponent {
  def init(
    client: Client,
  )(
    implicit
    contextShift: ContextShift[IO],
  ): IO[CamComponent] =
    for {
      textAreaCamHandler   <- Handler.create[String]
      radioInputCamHandler <- Handler.create[String]
      resultCamHandler     <- Handler.create[CamCompilerResult]
    } yield
      new CamComponent(
        textAreaCamHandler,
        radioInputCamHandler,
        resultCamHandler,
        client,
      )
}
