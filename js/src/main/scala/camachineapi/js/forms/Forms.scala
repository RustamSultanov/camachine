package camachineapi.js.forms

import cats.effect.IO
import monix.reactive.{Observable, Observer}
import outwatch.dom.BasicVNode
import outwatch.dom.dsl._
import cats.syntax.option._

object Forms {

  def buttonGroupRow[T](
    title: String,
    event: T,
    sink: Observer[T],
    disabledStream: Observable[Boolean],
  ): BasicVNode =
    div(
      cls := "form-group row",
      div(
        cls := "col-sm-10",
        button(
          `type` := "submit",
          cls := "btn btn-success",
          disabled <-- disabledStream,
          title,
          onClick.preventDefault(event) --> sink,
        ),
      ),
    )

  def buttonSubmitGroupRow[T, R](
    title: String,
    sink: Observer[R],
    disabledStream: Observable[Boolean],
    dataStream: Observable[T],
    handler: T => IO[R],
  ): BasicVNode =
    div(
      cls := "form-group row",
      div(
        cls := "col-sm-10",
        button(
          `type` := "submit",
          cls := "btn btn-success",
          disabled <-- disabledStream,
          title,
          onClick
            .preventDefault(dataStream)
            .transform(_.flatMap(data => Observable.from(handler(data)))) --> sink,
        ),
      ),
    )

  def radioGroupRow(
    sink: Observer[String],
    idValue: String,
    labelValue: String,
    initial: Option[String] = None,
    isRequired: Boolean = false,
  ): BasicVNode =
    div(
      cls := "form-group row",
      label(
        `for` := idValue,
        cls := "col-sm-2 col-form-label",
        labelValue,
      ),
      div(
        cls := "col-sm-10",
        input(
          `type` := "radio",
          cls := "form-control",
          id := idValue,
          onInput.value --> sink,
          required := isRequired,
        ),
      ),
    )

  def textAreaGroupRow(
    sink: Observer[String],
    idValue: String,
    labelValue: String,
    placeholderValue: String,
    initial: Option[String] = None,
    isRequired: Boolean = false,
  ): BasicVNode =
    div(
      cls := "form-group row",
      label(
        `for` := idValue,
        cls := "col-sm-2 col-form-label",
        labelValue,
      ),
      div(
        cls := "col-sm-10",
        textArea(
          cls := "form-control",
          attr("data-rows") := "10",
          attr("data-cols") := "140",
          placeholder := placeholderValue,
          id := idValue,
          onInput.value --> sink,
          initial,
          required := isRequired,
        ),
      ),
    )

}
