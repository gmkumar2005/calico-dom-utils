import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport.jsEnv

ThisBuild / organization := "io.github.gmkumar2005"
ThisBuild / scalaVersion := "3.3.1"
ThisBuild / tlBaseVersion := "0.2"
ThisBuild / scalacOptions ++= Seq("-new-syntax", "-indent", "-source:future")

lazy val calicodomutils = project
  .in(file("./calicodomutils"))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings)
  .settings(
    name := "calico-dom-utils"
  )

lazy val calicotestcases = project
  .in(file("./calicotestcases"))
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings)
  .settings(
    name := "calico-test-cases"
  )
  .dependsOn(calicodomutils)

lazy val root = project
  .settings(name := "calico-dom-utils")
  .in(file("."))
  .aggregate(calicodomutils, calicotestcases)

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel" %%% "cats-effect" % "3.5.3",
    "com.armanbilge" %%% "fs2-dom" % "0.2.1",
    "org.http4s" %%% "http4s-dom" % "0.2.10",
    "org.http4s" %%% "http4s-circe" % "0.23.23",
    "com.armanbilge" %%% "calico" % "0.2.2",
    "com.armanbilge" %%% "calico-router" % "0.2.2",
    "org.typelevel" %%% "cats-mtl" % "1.3.0",
    "org.scala-js" %%% "scalajs-dom" % "2.8.0",
    "dev.optics" %%% "monocle-core" % "3.2.0",
    "org.scalactic" %%% "scalactic" % "3.2.17",
    "org.scalatest" %%% "scalatest" % "3.2.17" % Test,
    "org.typelevel" %%% "cats-effect-testing-scalatest" % "1.5.0" % Test
  ),
  scalaJSLinkerConfig ~= {
    _.withModuleKind(ModuleKind.ESModule).withSourceMap(true)
  },
  scalaJSUseMainModuleInitializer := true,
//    jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv(),

  jsEnv := new jsenv.playwright.PWEnv(
    headless = true,
    debug = false,
    showLogs = false,
    browserName = "chromium"),
  Test / parallelExecution := true
)
