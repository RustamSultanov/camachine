package camachineapi.js

import camachineapi.js.components.{CamComponent, LambdaComponent}
import camachineapi.js.http.Client
import outwatch.dom.BasicVNode
import cats.effect.{ContextShift, IO}
import outwatch.dom.dsl._

final case class TopComponent(
  camComponent: CamComponent,
  lambdaComponent: LambdaComponent,
) {

  def node: BasicVNode =
    div(
      header(
        div(
          cls := "wrapper",
          div(
            cls := "jumbotron text-center",
            styleAttr := "margin-bottom:0",
            h1("Categorical Abstract Machine"),
          ),
        ),
      ),
      div(
        cls := "container",
        camComponent.node,
      ),
      div(
        cls := "container",
        lambdaComponent.node,
      ),
      footer(
        div(
          cls := "wrapper",
          div(
            cls := "jumbotron text-center",
            styleAttr := "margin-bottom:0",
            p("НИЯУ МИФИ 2019"),
          ),
        ),
      ),
    )

}

object TopComponent {

  def init(client: Client)(
    implicit
    contextShift: ContextShift[IO],
  ): IO[TopComponent] =
    for {
      camComponent    <- CamComponent.init(client)
      lambdaComponent <- LambdaComponent.init(client)
    } yield TopComponent(camComponent, lambdaComponent)

}
