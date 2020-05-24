package camachineapi.http

import camachineapi.CAMachine.Compiler
import camachineapi.CamApiEndpoints
import camachineapi.models.{
  CamCompilerData,
  CamCompilerResult,
  RecvarsDto,
  VariableDto,
}
import cats.effect.{ContextShift, IO}
import endpoints.http4s.server.{Endpoints, JsonEntitiesFromSchemas}
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

import scala.concurrent.ExecutionContext

object CamEndpoints
    extends Endpoints[IO]
    with JsonEntitiesFromSchemas
    with CamApiEndpoints {

  implicit val ioContextShift: ContextShift[IO] =
    IO.contextShift(ExecutionContext.global)

  def endpoints(): HttpRoutes[IO] = HttpRoutes.of[IO] {

    cam.implementedBy { data =>
      val compiler = new Compiler
      val memory = compiler.memory
      compiler.compile(data.commands, data.optimisation)
      val result = CamCompilerResult(
        memory.recvars.toList
          .map(
            recvar => RecvarsDto(recvar.getName, recvar.getValue.toString()),
          ),
        memory.variables.toList
          .map(
            variable =>
              VariableDto(variable.getName, variable.getValue.toString()),
          ),
        memory.getRows.toList,
        memory.getError,
      )
      result
    }

  }

}
