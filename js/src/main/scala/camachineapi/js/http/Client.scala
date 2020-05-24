package camachineapi.js.http

import camachineapi.js.syntax.http._
import camachineapi.models.{CamCompilerData, CamCompilerResult, LambdaCode}
import cats.effect.{ContextShift, IO}
import io.circe.parser.parse
import io.circe.syntax._
import monix.execution.Scheduler
import outwatch.http.Http
import outwatch.http.Http.Request

class Client(
  protocolHost: String,
)(
  implicit sheduler: Scheduler,
) {

  private def createCamRequest(data: CamCompilerData): Request =
    Request(
      url = s"$protocolHost/api/cam",
      data = data.asJson.toString,
    )

  private def camResult(
    response: Http.Response,
  ): Either[Exception, CamCompilerResult] =
    response.expectOk.flatMap(
      response =>
        parse(response.xhr.responseText)
          .flatMap(_.as[CamCompilerResult]),
    )

  def requestCam(
    data: CamCompilerData,
  )(
    implicit
    contextShift: ContextShift[IO],
  ): IO[CamCompilerResult] =
    IO.fromFuture(IO(Api.cam(data).toFuture))

  //***************

  private def createLambdaRequest(data: LambdaCode): Request =
    Request(
      url = s"$protocolHost/api/lambda",
      data = data.asJson.toString,
    )

  private def lambdaResult(
    response: Http.Response,
  ): Either[Exception, LambdaCode] =
    response.expectOk.flatMap(
      response =>
        parse(response.xhr.responseText)
          .flatMap(_.as[LambdaCode]),
    )

  def requestLambda(
    data: LambdaCode,
  )(
    implicit
    contextShift: ContextShift[IO],
  ): IO[LambdaCode] =
    IO.fromFuture(IO(Api.lambda(data).toFuture))

  //**********************
}
