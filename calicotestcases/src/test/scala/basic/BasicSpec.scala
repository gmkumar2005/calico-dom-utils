package basic

import calico.*
import calico.html.io.*
import calico.html.io.given
import cats.Monad
import cats.effect.IO
import cats.effect.Resource
import cats.effect.testing.scalatest.AsyncIOSpec
import fs2.dom.Dom
import fs2.dom.Element
import fs2.dom.Node
import fs2.dom.Window
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers.equal
import org.scalatest.matchers.should.Matchers.should

class BasicSpec extends AsyncFunSuite with AsyncIOSpec {
  // Prepare the DOM
  val appDiv: dom.Element = document.createElement("div")
  appDiv.id = "app"
  document.body.appendChild(appDiv)

  val rootElementId: String = "app"
  val window: Window[IO] = Window[IO]
  val rootElement: IO[Node[IO]] = window.document.getElementById(rootElementId).map(_.get)
  def mainApp(): IO[Node[IO]] = rootElement
  extension [F[_]](componentUnderTest: Resource[F, Node[F]])
    /**
     * Combines the component under test with a root element and mounts the component into the
     * root element.
     */
    def mountInto(rootElement: F[Node[F]])(using Monad[F], Dom[F]): Resource[F, Unit] = {
      Resource
        .eval(rootElement)
        .flatMap(root =>
          componentUnderTest.flatMap(e =>
            Resource.make(root.appendChild(e))(_ => root.removeChild(e))))
    }

  test("renders empty elements") {
    val empty_div: Resource[IO, Element[IO]] = div("")
    empty_div.mountInto(mainApp()).surround {
      IO {
        val expectedEl = document.createElement("div")
        val actual = dom.document.querySelector("#app > div")
        assert(actual != null, "querySelector returned null. Check if the query is correct")
        actual.outerHTML should equal(expectedEl.outerHTML)
      }
    }
  }
}
