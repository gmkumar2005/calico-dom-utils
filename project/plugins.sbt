val sbtTypelevelVersion = "0.6.4"
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.15.0")
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.21.1")
libraryDependencies += "org.scala-js" %% "scalajs-env-jsdom-nodejs" % "1.1.0"
libraryDependencies += "io.github.gmkumar2005" %% "scala-js-env-playwright" % "0.1.12"
addSbtPlugin("org.typelevel" % "sbt-typelevel" % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-scalafix" % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-site" % sbtTypelevelVersion)