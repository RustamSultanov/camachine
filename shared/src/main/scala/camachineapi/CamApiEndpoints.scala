package camachineapi

import camachineapi.CAMachine.Row
import camachineapi.models.{
  CamCompilerData,
  CamCompilerResult,
  LambdaCode,
  RecvarsDto,
  VariableDto,
}
import endpoints.algebra
import endpoints.algebra.Endpoints
import endpoints.generic.JsonSchemas

trait CamApiEndpoints
    extends Endpoints
    with algebra.JsonEntitiesFromSchemas
    with JsonSchemas {

  val cam: Endpoint[CamCompilerData, CamCompilerResult] =
    endpoint(
      post(path / "api" / "cam", jsonRequest[CamCompilerData]),
      ok(jsonResponse[CamCompilerResult]),
    )
  val lambda: Endpoint[LambdaCode, LambdaCode] =
    endpoint(
      post(path / "api" / "lambda", jsonRequest[LambdaCode]),
      ok(jsonResponse[LambdaCode]),
    )

  implicit lazy val recvarsSchema: JsonSchema[RecvarsDto] =
    genericJsonSchema[RecvarsDto]
  implicit lazy val varSchema: JsonSchema[VariableDto] =
    genericJsonSchema[VariableDto]
  implicit lazy val rowSchema: JsonSchema[Row] = genericJsonSchema[Row]
  implicit lazy val camDataSchema: JsonSchema[CamCompilerData] =
    genericJsonSchema[CamCompilerData]
  implicit lazy val camResultSchema: JsonSchema[CamCompilerResult] =
    genericJsonSchema[CamCompilerResult]
  implicit lazy val lambdaSchema: JsonSchema[LambdaCode] =
    genericJsonSchema[LambdaCode]

}
