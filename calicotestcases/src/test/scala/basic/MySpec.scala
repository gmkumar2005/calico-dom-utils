package basic

import calico.html.io.*
import calico.html.io.given
import cats.effect.IO
import cats.effect.Resource
import fs2.dom.Element
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalatest.matchers.must.Matchers.*
import utils.CalicoSpec

class MySpec extends CalicoSpec {
  test("renders empty elements") {
    val empty_div: Resource[IO, Element[IO]] = div("")
    empty_div.mountInto(mainApp()).surround {
      IO {
        val expectedEl = document.createElement("div")
        val actual = dom.document.querySelector("#app > div")
        assert(actual != null, "querySelector returned null. Check if the query is correct")
        actual.outerHTML should equal(expectedEl.outerHTML)
      }
    } *> {
      val empty_span: Resource[IO, Element[IO]] = span("")
      empty_span.mountInto(mainApp()).surround {
        IO {
          val expectedEl = document.createElement("span")
          val actual = dom.document.querySelector("#app > span")
          assert(actual != null, "querySelector returned null. Check if the query is correct")
          actual.outerHTML should equal(expectedEl.outerHTML)
        }
      }
    }
  }
}
