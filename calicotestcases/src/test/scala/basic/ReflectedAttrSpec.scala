package basic

import calico.html.io.*
import calico.html.io.given
import calico.syntax.*
import cats.effect.IO
import cats.effect.Resource
import cats.syntax.all.*
import org.scalajs.dom
import org.scalajs.dom.document
import utils.CalicoSpec

import scala.util.Random

class ReflectedAttrSpec extends CalicoSpec {

  test("sets reflected attrs") {
    val expectedRel = randomString("rel_")

    val rel_div = div("").flatTap(_.modify(rel := List(expectedRel)))
    rel_div.mountInto(mainApp()).surround {
      IO {
        val expectedEl = document.createElement("div").asInstanceOf[dom.html.Input]
        expectedEl.setAttribute("rel", expectedRel)
        val actual = dom.document.querySelector("#app > div").asInstanceOf[dom.html.Input]
        assert(actual != null, "querySelector returned null check if the query is correct")
        actual.outerHTML should equal(expectedEl.outerHTML)
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
      .mountInto(mainApp())
      .surround {
        IO {
          val actual = dom.document.querySelector("#app > link").asInstanceOf[dom.html.Link]
          assert(actual != null, "querySelector returned null check if the query is correct")
          actual.outerHTML should equal(expectedEl.outerHTML)
        }
      }
      .flatMap { _ =>
        val href_link_rev = linkTag(href := expectedHref, rel := List(expectedRel))
        href_link_rev.mountInto(mainApp()).surround {
          IO {
            val actual = dom.document.querySelector("#app > link").asInstanceOf[dom.html.Link]
            assert(actual != null, "querySelector returned null check if the query is correct")
            actual.outerHTML should not equal expectedEl.outerHTML
          }
        }
      }
  }
  test("sets non-string reflected attrs") {
    val disabled_input = input(disabled := true)
    disabled_input
      .mountInto(mainApp())
      .surround {
        IO {
          val expectedEl = document.createElement("input").asInstanceOf[dom.html.Input]
          expectedEl.setAttribute("disabled", "")
          val actual = dom.document.querySelector("#app > input").asInstanceOf[dom.html.Input]
          assert(actual != null, "querySelector returned null check if the query is correct")
          actual.outerHTML should equal(expectedEl.outerHTML)
        }
      }
      .flatMap { _ =>
        val disabled_input_false = input(disabled := false)
        disabled_input_false
          .mountInto(mainApp())
          .surround {
            IO {
              val expectedEl = document.createElement("input").asInstanceOf[dom.html.Input]
              val actual =
                dom.document.querySelector("#app > input").asInstanceOf[dom.html.Input]
              assert(
                actual != null,
                "querySelector returned null check if the query is correct")
              actual.outerHTML should equal(expectedEl.outerHTML)
            }
          }
          .flatMap { _ =>
            val expectedColSpan = 1 + Random.nextInt(10)
            val colSpan_td = td(colSpan := expectedColSpan)
            colSpan_td.mountInto(mainApp()).surround {
              IO {
                val expectedEl = document.createElement("td").asInstanceOf[dom.html.TableCell]
                expectedEl.setAttribute("colspan", expectedColSpan.toString)
                val actual =
                  dom.document.querySelector("#app > td").asInstanceOf[dom.html.TableCell]
                assert(
                  actual != null,
                  "querySelector returned null check if the query is correct")
                actual.outerHTML should equal(expectedEl.outerHTML)
              }
            }

          }
      }
  }

  test("sets reflected attrs in nested elements") {
    val text_span = span("hello")
    val expectedColSpan = 1 + Random.nextInt(10)
    val expectedRowSpan = 15 + Random.nextInt(7)
    val colSpan_td = td(colSpan := expectedColSpan, rowSpan := expectedRowSpan, text_span)
    colSpan_td.mountInto(mainApp()).surround {
      IO {
        val expectedEl = document.createElement("td").asInstanceOf[dom.html.TableCell]
        val expectedSpan = document.createElement("span").asInstanceOf[dom.html.Span]
        expectedSpan.textContent = "hello"
        expectedEl.setAttribute("colspan", expectedColSpan.toString)
        expectedEl.appendChild(expectedSpan)
        expectedEl.setAttribute("rowspan", expectedRowSpan.toString)

        val actual =
          dom.document.querySelector("#app > td").asInstanceOf[dom.html.TableCell]
        assert(actual != null, "querySelector returned null check if the query is correct")
        actual.outerHTML should equal(expectedEl.outerHTML)
//        assert(display.block.value == "block")
      }
    }
  }

}
