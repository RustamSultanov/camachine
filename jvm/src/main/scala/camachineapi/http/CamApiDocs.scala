package camachineapi.http

import camachineapi.CamApiEndpoints
import endpoints.openapi
import endpoints.openapi.model.{Info, OpenApi}

object CamApiDocs
    extends CamApiEndpoints
    with openapi.Endpoints
    with openapi.JsonEntitiesFromSchemas {

  val api: OpenApi =
    openApi(
      Info(title = "Category Abstract Machine API", version = "1.0.0"),
    )(cam, lambda)
}
