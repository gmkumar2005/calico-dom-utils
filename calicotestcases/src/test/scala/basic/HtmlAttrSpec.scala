package basic

import calico.html.io.*
import calico.html.io.given
import calico.html.io.colSpan
import calico.html.io.div
import calico.html.io.rowSpan
import calico.html.io.title
import calico.syntax.*
import cats.effect.IO
import cats.effect.Resource
import cats.syntax.all.*
import fs2.dom.Element
import org.scalajs.dom
import org.scalajs.dom.document
import utils.CalicoSpec

import scala.util.Random

class HtmlAttrSpec extends CalicoSpec {

  test("sets attrs") {
    val expectedTitle = randomString("title_")
    val expectedColSpan = 1 + Random.nextInt(15)
    val expectedRowSpan = 15 + Random.nextInt(7)
    val title_div: Resource[IO, Element[IO]] = div("").flatTap(_.modify(title := expectedTitle))

    title_div
      .mountInto(mainApp())
      .surround {
        IO {
          val expectedEl = document.createElement("div")
          expectedEl.setAttribute("title", expectedTitle)
          val actual = dom.document.querySelector("#app > div")
          assert(actual != null, "querySelector returned null check if the query is correct")
          actual.outerHTML should equal(expectedEl.outerHTML)
        }
      }
      .flatMap { _ =>
        val colSpan_td = td("")
          .flatTap(_.modify(colSpan := expectedColSpan))
          .flatTap(_.modify(rowSpan := expectedRowSpan))
        colSpan_td.mountInto(mainApp()).surround {
          IO {
            val expectedEl = document.createElement("td")
            expectedEl.setAttribute("colspan", expectedColSpan.toString)
            expectedEl.setAttribute("rowspan", expectedRowSpan.toString)
            val actual = dom.document.querySelector("#app > td")
            assert(actual != null, "querySelector returned null check if the query is correct")
            actual.outerHTML should equal(expectedEl.outerHTML)
          }
        }
      }

  }

  test("sets boolean attrs") {
    val editable_div = div("").flatTap(_.modify(contentEditable := true))
    editable_div.mountInto(mainApp()).surround {
      IO {
        val expectedEl = document.createElement("div")
        expectedEl.setAttribute("contenteditable", "true")
        val actual = dom.document.querySelector("#app > div")
        assert(actual != null, "querySelector returned null check if the query is correct")
        actual.outerHTML should equal(expectedEl.outerHTML)
      }
    }
  }
  test("sets integer attrs") {
    val height_td = td("").flatTap(_.modify(heightAttr := 100))
    height_td.mountInto(mainApp()).surround {
      IO {
        val expectedEl = document.createElement("td")
        expectedEl.setAttribute("height", "100")
        val actual = dom.document.querySelector("#app > td")
        assert(actual != null, "querySelector returned null check if the query is correct")
        actual.outerHTML should equal(expectedEl.outerHTML)
      }
    }
  }

}
