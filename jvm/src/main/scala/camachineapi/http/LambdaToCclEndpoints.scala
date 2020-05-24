package camachineapi.http

import camachineapi.CamApiEndpoints
import camachineapi.LambdaToCCL.{DeBruijnToCcl, SimpleLambdaParser, Term}
import camachineapi.models.LambdaCode
import cats.effect.{ContextShift, IO}
import endpoints.http4s.server.{Endpoints, JsonEntitiesFromSchemas}
import org.http4s.HttpRoutes

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

object LambdaToCclEndpoints
    extends Endpoints[IO]
    with JsonEntitiesFromSchemas
    with CamApiEndpoints {

  implicit val ioContextShift: ContextShift[IO] =
    IO.contextShift(ExecutionContext.global)

  def endpoints(): HttpRoutes[IO] = HttpRoutes.of[IO] {
    lambda.implementedBy { data =>
      val maybeTerm: Try[Term] = Try(SimpleLambdaParser.parse(data.code))
      val result = maybeTerm match {
        case Success(term) =>
          LambdaCode(
            term.toString + " -> " + DeBruijnToCcl.toCCL(term).toString,
          )
        case Failure(ex) => LambdaCode(ex.getMessage)
      }
      result
    }
  }

}
