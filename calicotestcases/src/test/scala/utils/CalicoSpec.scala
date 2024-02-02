package utils

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import fs2.dom.Dom
import fs2.dom.Node
import fs2.dom.Window
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers

abstract class CalicoSpec extends AsyncFunSuite with Matchers with AsyncIOSpec with Utils {
  val appDiv: dom.Element = document.createElement("div")
  appDiv.id = "app"
  document.body.appendChild(appDiv)

  val rootElementId: String = "app"
  val window: Window[IO] = Window[IO]
  val rootElement: IO[Node[IO]] = window.document.getElementById(rootElementId).map(_.get)
  def mainApp(): IO[Node[IO]] = rootElement

}
