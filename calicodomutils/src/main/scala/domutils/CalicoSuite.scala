package domutils

import calico.*
import calico.html.io.given
import cats.Monad
import cats.effect.IO
import cats.effect.Resource
import fs2.dom.Dom
import fs2.dom.Node
import fs2.dom.Window
import munit.Assertions.assertEquals
import munit.Assertions.fail
import munit.CatsEffectSuite
import munit.Compare
import munit.Location
import org.scalajs.dom
import org.scalajs.dom.document

trait CalicoSuite extends CatsEffectSuite {
  val appDiv: dom.Element = document.createElement("div")
  appDiv.id = "app"
  document.body.appendChild(appDiv)

  val rootElementId: String = "app"
  val window: Window[IO] = Window[IO]
  val rootElement: IO[Node[IO]] = window.document.getElementById(rootElementId).map(_.get)

  def assertQuery(queryString: String, expected: Int, clue: => Any = "values are not the same")(
      implicit loc: Location): Unit = {

    val element = org.scalajs.dom.document.querySelectorAll(queryString)
    element match
      case nodeList: org.scalajs.dom.NodeList[org.scalajs.dom.Element] =>
        assertEquals(nodeList.length, expected, clue)
      case null =>
        fail("not a node list")

  }
}
