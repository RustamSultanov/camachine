package camachineapi.js.http

import camachineapi.CamApiEndpoints
import endpoints.xhr._

object Api
    extends Endpoints
    with JsonEntitiesFromSchemas
    with CamApiEndpoints
    with thenable.Endpoints
