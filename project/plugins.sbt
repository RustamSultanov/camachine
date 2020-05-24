addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.0.0")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.33")

// we target Scala.js 0.6 because of cats: https://github.com/typelevel/cats/issues/2195
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.15.0-0.6") // scala-steward:off

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.7.2")
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
addSbtPlugin("com.heroku" % "sbt-heroku" % "2.1.4")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.0")

