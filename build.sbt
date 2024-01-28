import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport.jsEnv

ThisBuild / organization := "io.github.gmkumar2005"
ThisBuild / scalaVersion := "3.3.1"

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
    // Generated scala.js output will call your main() method to start your app.

  )
  .dependsOn(calicodomutils)

lazy val root = project
  .in(file("."))
  .aggregate(calicodomutils, calicotestcases)

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-deprecation",
    "-language:implicitConversions"
  ),
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
    "org.typelevel" %%% "scalacheck-effect-munit" % "2.0.0-M2",
    "org.typelevel" %% "cats-effect-testkit" % "3.5.3",
    "org.typelevel" %%% "munit-cats-effect" % "2.0.0-M4",
    "org.scalactic" %%% "scalactic" % "3.2.17",
    "com.raquo" %%% "domtestutils" % "18.0.1" // Scala.js
  ),
  scalaJSUseMainModuleInitializer := true,
  jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()
//    jsEnv := new jsenv.playwright.PWEnv()
)
