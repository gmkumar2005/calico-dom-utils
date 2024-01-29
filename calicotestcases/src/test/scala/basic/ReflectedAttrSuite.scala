package basic

import calico.html.io.*
import calico.html.io.given
import calico.syntax.*
import cats.effect.IO
import cats.syntax.all.*
import domutils.CalicoSuite
import domutils.Utils.randomAlphaNumeric
import domutils.Utils.randomString
import munit.CatsEffectSuite
import org.scalajs.dom
import org.scalajs.dom.document

class ReflectedAttrSuite extends CalicoSuite {
  test("sets reflected attrs") {
    val expectedRel = randomString("rel_")
//    val expectedHref = randomString("href_")
//    val expectedAlt = randomString("alt_")

    val rel_div = div("").flatTap(_.modify(rel := List(expectedRel)))
    rel_div.mountInto(rootElement).surround {
      IO {
        val expectedEl = document.createElement("div").asInstanceOf[dom.html.Input]
        expectedEl.setAttribute("rel", expectedRel)
        val actual = dom.document.querySelector("#app > div").asInstanceOf[dom.html.Input]
        assert(actual != null, "querySelector returned null check if the query is correct")
        assertEquals(actual.outerHTML, expectedEl.outerHTML)
      }
    }

  }

  test("order of modifiers does not matter") {
    val expectedHref = randomAlphaNumeric("href_")
    val expectedRel = randomAlphaNumeric("rel_")

    val expectedEl = document.createElement("link").asInstanceOf[dom.html.Link]

    expectedEl.setAttribute("rel", expectedRel)
    expectedEl.setAttribute("href", expectedHref)
    val href_link = linkTag(rel := List(expectedRel), href := expectedHref)
    href_link
      .mountInto(rootElement)
      .surround {
        IO {
          val actual = dom.document.querySelector("#app > link").asInstanceOf[dom.html.Link]
          assert(actual != null, "querySelector returned null check if the query is correct")
          assertEquals(actual.outerHTML, expectedEl.outerHTML)
        }
      }
      .flatMap { _ =>
        val href_link_rev = linkTag(href := expectedHref, rel := List(expectedRel))
        href_link_rev.mountInto(rootElement).surround {
          IO {
            val actual = dom.document.querySelector("#app > link").asInstanceOf[dom.html.Link]
            assert(actual != null, "querySelector returned null check if the query is correct")
            assertNotEquals(actual.outerHTML, expectedEl.outerHTML)
          }
        }
      }
  }
  test("sets non-string reflected attrs") {
    val disabled_input = input(disabled := true)
    val disabled_input_false = input(disabled := false)
    disabled_input
      .mountInto(rootElement)
      .surround {
        IO {
          val expectedEl = document.createElement("input").asInstanceOf[dom.html.Input]
          expectedEl.setAttribute("disabled", "")
          val actual = dom.document.querySelector("#app > input").asInstanceOf[dom.html.Input]
          assert(actual != null, "querySelector returned null check if the query is correct")
          assertEquals(actual.outerHTML, expectedEl.outerHTML)
        }
      }
      .flatMap { _ =>
        disabled_input_false.mountInto(rootElement).surround {
          IO {
            val expectedEl = document.createElement("input").asInstanceOf[dom.html.Input]
            val actual = dom.document.querySelector("#app > input").asInstanceOf[dom.html.Input]
            assert(actual != null, "querySelector returned null check if the query is correct")
            assertEquals(actual.outerHTML, expectedEl.outerHTML)
          }
        }
      }
  }
}
