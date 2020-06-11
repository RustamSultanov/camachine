package camachineapi.js.http

import camachineapi.models.{CamCompilerData, CamCompilerResult, LambdaCode}
import cats.effect.{ContextShift, IO}
import monix.execution.Scheduler

class Client(
  protocolHost: String,
)(
  implicit sheduler: Scheduler,
) {

  def requestCam(
    data: CamCompilerData,
  )(
    implicit
    contextShift: ContextShift[IO],
  ): IO[CamCompilerResult] =
    IO.fromFuture(IO(Api(protocolHost).cam(data).toFuture))

  def requestLambda(
    data: LambdaCode,
  )(
    implicit
    contextShift: ContextShift[IO],
  ): IO[LambdaCode] =
    IO.fromFuture(IO(Api(protocolHost).lambda(data).toFuture))

}
