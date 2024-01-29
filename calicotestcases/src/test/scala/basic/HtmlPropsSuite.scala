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
    val checked_input =
      input("").flatTap(_.modify(typ := "checkbox")).flatTap(_.modify(checked := false))
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

    val value_of_input = input("").flatTap(_.modify(value := "yolo"))
    value_of_input.mountInto(rootElement).surround {
      IO {
        val expectedEl = document.createElement("input").asInstanceOf[dom.html.Input]
        expectedEl.value = "yolo"
        val actual = dom.document.querySelector("#app > input").asInstanceOf[dom.html.Input]
        assert(actual != null, "querySelector returned null check if the query is correct")
        assertEquals(actual.outerHTML, expectedEl.outerHTML)
        assertEquals(actual.value, expectedEl.value)
      }
    }

    val true_option =
      option("true").flatTap(_.modify(selected := true)).flatTap(_.modify(value := "123"))
    true_option.mountInto(rootElement).surround {
      IO {
        val expectedEl = document.createElement("option").asInstanceOf[dom.html.Option]
        expectedEl.selected = true
        expectedEl.textContent = "true"
        expectedEl.value = "123"
        val actual = dom.document.querySelector("#app > option").asInstanceOf[dom.html.Option]
        assert(actual != null, "querySelector returned null check if the query is correct")
        assertEquals(actual.outerHTML, expectedEl.outerHTML)
        assertEquals(actual.selected, expectedEl.selected)
        assertEquals(actual.textContent, expectedEl.textContent)
      }
    }

    val false_option =
      option("false").flatTap(_.modify(selected := false)).flatTap(_.modify(value := "123"))
    false_option.mountInto(rootElement).surround {
      IO {
        val expectedEl = document.createElement("option").asInstanceOf[dom.html.Option]
        expectedEl.selected = false
        expectedEl.textContent = "false"
        expectedEl.value = "123"
        val actual = dom.document.querySelector("#app > option").asInstanceOf[dom.html.Option]
        assert(actual != null, "querySelector returned null check if the query is correct")
        assertEquals(actual.outerHTML, expectedEl.outerHTML)
        assertEquals(actual.selected, expectedEl.selected)
        assertEquals(actual.textContent, expectedEl.textContent)
      }
    }

    val div_input =
      div(input("").flatTap(_.modify(typ := "checkbox")).flatTap(_.modify(checked := false)))
    div_input.mountInto(rootElement).surround {
      IO {
        val expectedEl = document.createElement("div")
        val inputEl = document.createElement("input").asInstanceOf[dom.html.Input]
        inputEl.checked = false
        inputEl.setAttribute("type", "checkbox")
        expectedEl.appendChild(inputEl)
        val actual = dom.document.querySelector("#app > div")
        assert(actual != null, "querySelector returned null check if the query is correct")
        assertEquals(actual.outerHTML, expectedEl.outerHTML)
        val actualInput =
          dom.document.querySelector("#app > div > input").asInstanceOf[dom.html.Input]
        assertEquals(actualInput.outerHTML, inputEl.outerHTML)
        assertEquals(actualInput.checked, inputEl.checked)
      }
    }

  }
}
