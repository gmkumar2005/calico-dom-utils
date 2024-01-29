package basic

import calico.*
import calico.*
import calico.html.io.colSpan
import calico.html.io.div
import calico.html.io.label
import calico.html.io.rowSpan
import calico.html.io.title
import calico.html.io.{*, given}
import calico.html.io.{*, given}
import calico.syntax.*
import cats.effect.IO
import cats.effect.Resource
import cats.effect.syntax.all.*
import cats.syntax.all.*
import domutils.CalicoSuite
import domutils.Utils.randomString
import fs2.dom.Element
import munit.CatsEffectSuite
import org.scalajs.dom
import org.scalajs.dom.document

import scala.util.Random

class HtmlAttrSuite extends CalicoSuite {

  test("sets attrs") {
    val expectedTitle = randomString("title_")
    val expectedColSpan = 1 + Random.nextInt(15)
    val expectedRowSpan = 15 + Random.nextInt(7)
    val title_div: Resource[IO, Element[IO]] = div("").flatTap(_.modify(title := expectedTitle))

    title_div.mountInto(rootElement).surround {
      IO {
        val expectedEl = document.createElement("div")
        expectedEl.setAttribute("title", expectedTitle)
        val actual = dom.document.querySelector("#app > div")
        assert(actual != null, "querySelector returned null check if the query is correct")
        assertEquals(actual.outerHTML, expectedEl.outerHTML)
      }
    }

    val colSpan_td = td("")
      .flatTap(_.modify(colSpan := expectedColSpan))
      .flatTap(_.modify(rowSpan := expectedRowSpan))
    colSpan_td.mountInto(rootElement).surround {
      IO {
        val expectedEl = document.createElement("td")
        expectedEl.setAttribute("colspan", expectedColSpan.toString)
        expectedEl.setAttribute("rowspan", expectedRowSpan.toString)
        val actual = dom.document.querySelector("#app > td")
        assert(actual != null, "querySelector returned null check if the query is correct")
        assertEquals(actual.outerHTML, expectedEl.outerHTML)
      }
    }

  }

  test("sets boolean attrs") {
    val editable_div = div("").flatTap(_.modify(contentEditable := true))
    editable_div.mountInto(rootElement).surround {
      IO {
        val expectedEl = document.createElement("div")
        expectedEl.setAttribute("contenteditable", "true")
        val actual = dom.document.querySelector("#app > div")
        assert(actual != null, "querySelector returned null check if the query is correct")
        assertEquals(actual.outerHTML, expectedEl.outerHTML)
      }
    }
  }
  test("sets integer attrs") {
    val height_td = td("").flatTap(_.modify(heightAttr := 100))
    height_td.mountInto(rootElement).surround {
      IO {
        val expectedEl = document.createElement("td")
        expectedEl.setAttribute("height", "100")
        val actual = dom.document.querySelector("#app > td")
        assert(actual != null, "querySelector returned null check if the query is correct")
        assertEquals(actual.outerHTML, expectedEl.outerHTML)
      }
    }
  }

}
