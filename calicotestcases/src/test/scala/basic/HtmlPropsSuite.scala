package basic

import calico.html.io.{*, given}
import calico.syntax.*
import cats.effect.IO
import cats.syntax.all.*
import domutils.CalicoSuite
import munit.CatsEffectSuite
import org.scalajs.dom
import org.scalajs.dom.document

class HtmlPropsSuite extends CalicoSuite {
  test("sets props") {
    val checked_input = input("").flatTap(_.modify(typ := "checkbox")).flatTap(_.modify(checked := false))
    checked_input.mountInto(rootElement).surround {
      IO {
        val expectedEl = document.createElement("input").asInstanceOf[dom.html.Input]
        expectedEl.checked = false
        expectedEl.setAttribute("type", "checkbox")
        val actual = dom.document.querySelector("#app > input").asInstanceOf[dom.html.Input]
        assert(actual != null, "querySelector returned null check if the query is correct")
        assertEquals(actual.outerHTML, expectedEl.outerHTML)
        assertEquals(actual.checked, expectedEl.checked)
      }
    }
  }
}
