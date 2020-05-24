val Http4sVersion = "0.21.4"
val LogbackVersion = "1.2.3"
val endpointsVersion  = "0.15.0"
val tapirVersion = "0.15.0"

import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

lazy val app =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Full).in(file("."))
    .settings(
      organization := "ru.psttf",
      scalaVersion := "2.13.1",
      version := "0.1.0-SNAPSHOT",
      libraryDependencies ++= Seq(
        "org.typelevel" %%% "mouse" % "0.24",
        "io.circe" %%% "circe-generic" % "0.13.0",
        "io.circe" %%% "circe-literal" % "0.13.0",
        "io.circe" %%% "circe-generic-extras" % "0.13.0",
        "io.circe" %%% "circe-parser" % "0.13.0",
        "com.beachape" %%% "enumeratum-circe" % "1.6.0",
        "com.outr" %%% "scribe" % "2.7.10",
        "org.specs2" %%% "specs2-core" % "4.8.1" % Test,
        "org.julienrf" %%% "endpoints-algebra" % endpointsVersion,
        "org.julienrf" %%% "endpoints-json-schema-circe" % endpointsVersion,
        "org.julienrf" %%% "endpoints-json-schema-generic" %endpointsVersion,
        "org.julienrf" %%% "endpoints-algebra-json-schema" %  endpointsVersion,
      ),
      scalacOptions ++= Seq(
        "-Xlint",
        "-unchecked",
        "-deprecation",
        "-feature",
        "-language:higherKinds",
        "-Ymacro-annotations",
        "-Ywarn-macros:after",

      ),
      scalafmtOnCompile := true,
    )

lazy val appJS = app.js
  .enablePlugins(ScalaJSBundlerPlugin)
  .disablePlugins(RevolverPlugin)
  .settings(
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      "jitpack" at "https://jitpack.io",
    ),
    libraryDependencies ++= Seq(
      "com.github.OutWatch.outwatch" %%% "outwatch" % "584f3f2c32",
      "org.julienrf" %%% "endpoints-xhr-client" % endpointsVersion,
    ),
    npmDependencies in Compile ++= Seq(
      "jquery" -> "3.3",
      "bootstrap" -> "4.3",
    ),
    webpackBundlingMode := BundlingMode.LibraryAndApplication(), // LibraryOnly() for faster dev builds
    scalaJSUseMainModuleInitializer := true,
    mainClass in Compile := Some("camachineapi.js.Main"),
    useYarn := true, // makes scalajs-bundler use yarn instead of npm
  )

lazy val appJVM = app.jvm
  .enablePlugins(JavaAppPackaging)
  .settings(
    (unmanagedResourceDirectories in Compile) += (resourceDirectory in(appJS, Compile)).value,
    mappings.in(Universal) ++= webpack.in(Compile, fullOptJS).in(appJS, Compile).value.map { f =>
      f.data -> s"assets/${f.data.getName}"
    },
    mappings.in(Universal) ++= Seq(
      (target in(appJS, Compile)).value / ("scala-" + scalaBinaryVersion.value) / "scalajs-bundler" / "main" / "node_modules" / "bootstrap" / "dist" / "css" / "bootstrap.min.css" ->
        "assets/bootstrap/dist/css/bootstrap.min.css",
      (target in(appJS, Compile)).value / ("scala-" + scalaBinaryVersion.value) / "scalajs-bundler" / "main" / "node_modules" / "jquery" / "dist" / "jquery.slim.min.js" ->
        "assets/jquery/dist/jquery.slim.min.js",
      (target in(appJS, Compile)).value / ("scala-" + scalaBinaryVersion.value) / "scalajs-bundler" / "main" / "node_modules" / "popper.js" / "dist" / "umd/popper.min.js" ->
        "assets/popper.js/dist/umd/popper.min.js",
      (target in(appJS, Compile)).value / ("scala-" + scalaBinaryVersion.value) / "scalajs-bundler" / "main" / "node_modules" / "bootstrap" / "dist" / "js/bootstrap.min.js" ->
        "assets/bootstrap/dist/js/bootstrap.min.js",
    ),
    bashScriptExtraDefines += """addJava "-Dassets=${app_home}/../assets"""",
    mainClass in reStart := Some("camachineapi.Main"),
    libraryDependencies ++= Seq(
      "org.julienrf" %% "endpoints-http4s-server" % endpointsVersion,
      "org.webjars"            % "swagger-ui"                      % "3.20.9",
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-http4s" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"             % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml"       % tapirVersion,
      "org.typelevel" %% "cats-core" % "2.1.1",
      "org.typelevel" %% "cats-effect" % "2.1.3",
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "com.github.pureconfig" %% "pureconfig" % "0.12.1",
      "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
    ),
  )

disablePlugins(RevolverPlugin)

val openDev =
  taskKey[Unit]("open index-dev.html")

openDev := {
  val url = baseDirectory.value / "index-dev.html"
  streams.value.log.info(s"Opening $url in browser...")
  java.awt.Desktop.getDesktop.browse(url.toURI)
}

herokuAppName in Compile := "cammachine-api"

target in Compile := (target in(appJVM, Compile)).value
