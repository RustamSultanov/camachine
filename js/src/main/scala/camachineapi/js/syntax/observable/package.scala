package camachineapi.js.syntax

import cats.effect.IO
import monix.reactive.Observable
import org.scalajs.dom

import scala.util.Try

package object observable {

  implicit class ObservableOps[A](val observable: Observable[A])
      extends AnyVal {
    def domConsoleDebug(f: A => String): Observable[A] =
      for {
        o <- observable
        _ <- Observable.from(IO(dom.console.debug(f(o))))
      } yield o
    def domConsoleDebug(prefix: String): Observable[A] =
      domConsoleDebug(v => s"$prefix: $v")
  }

  implicit class StringObservableOps(val observable: Observable[String])
      extends AnyVal {
    def asInt: Observable[Int] =
      observable.flatMap(str => Observable.fromTry(Try(str.toInt)))
  }

}
