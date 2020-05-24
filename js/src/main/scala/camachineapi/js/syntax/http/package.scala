package camachineapi.js.syntax

import outwatch.http.Http.Response

package object http {

  implicit class ResponseOps(response: Response) {

    def expectStatus(expected: Int): Either[Exception, Response] =
      Either
        .cond(
          response.status == expected,
          response,
          new Exception(
            s"Unexpected status ${response.status}: ${response.body}",
          ),
        )

    def expectOk: Either[Exception, Response] = expectStatus(200)

  }

}
