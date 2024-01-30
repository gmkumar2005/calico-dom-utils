package basic

import calico.*
import calico.syntax.*
import calico.html.io.{*, given}
import cats.effect.{IO, Resource}
import domutils.CalicoSuite
import domutils.Utils.randomString
import fs2.dom.Element
import fs2.dom.Node
import munit.CatsEffectSuite
import munit.catseffect.IOFixture
import org.scalajs.dom
import org.scalajs.dom.document

class ElementsSuite extends CalicoSuite {
  private val text1 = randomString("text1_")
  private val text2 = randomString("text2_")
  private val text3 = randomString("text3_")
  val mainApp: IOFixture[Node[IO]] = ResourceSuiteLocalFixture(
    "main-app",
    Resource.eval(rootElement)
  )

  override def munitFixtures = List(mainApp)

  test("renders empty elements") {
    val my_div: Resource[IO, org.scalajs.dom.Element] =
      IO(org.scalajs.dom.document.createElement("div")).toResource

    val empty_div: Resource[IO, Element[IO]] = div("")
    empty_div.renderInto(mainApp()).surround {
      IO {
        val expectedEl = document.createElement("div")
        val actual = dom.document.querySelector("#app > div")
        assert(actual != null, "querySelector returned null check if the query is correct")
        assertEquals(actual.outerHTML, expectedEl.outerHTML)
      }
    } *> {
      val empty_span: Resource[IO, Element[IO]] = span("")
      empty_span.renderInto(mainApp()).surround {
        IO {
          val expectedEl = document.createElement("span")
          val actual = dom.document.querySelector("#app > span")
          assert(actual != null, "querySelector returned null check if the query is correct")
          assertEquals(actual.outerHTML, expectedEl.outerHTML)
        }
      }
    } *> {
      val empty_p: Resource[IO, Element[IO]] = p("")
      empty_p.renderInto(mainApp()).surround {
        IO {
          val expectedEl = document.createElement("p")
          val actual = dom.document.querySelector("#app > p")
          assert(actual != null, "querySelector returned null check if the query is correct")
          assertEquals(actual.outerHTML, expectedEl.outerHTML)
        }
      }
    } *> {
      val empty_hr: Resource[IO, Element[IO]] = hr("")
      empty_hr.renderInto(mainApp()).surround {
        IO {
          val expectedEl = document.createElement("hr")
          val actual = dom.document.querySelector("#app > hr")
          assert(actual != null, "querySelector returned null check if the query is correct")
          assertEquals(actual.outerHTML, expectedEl.outerHTML)
        }
      }
    }

  }

  test("renders elements with text Content") {
    val span_tag = span(text1)
    span_tag.renderInto(mainApp()).surround {
      IO {
        val expectedEl = document.createElement("span")
        expectedEl.textContent = text1
        val actual = dom.document.querySelector("#app > span")
        assert(actual != null, "querySelector returned null check if the query is correct")
        assertEquals(actual.outerHTML, expectedEl.outerHTML)
      } *> {
        val article_tag = articleTag(text1)
        article_tag.renderInto(mainApp()).surround {
          IO {
            val expectedEl = document.createElement("article")
            expectedEl.textContent = text1
            val actual = dom.document.querySelector("#app > article")
            assert(actual != null, "querySelector returned null check if the query is correct")
            assertEquals(actual.outerHTML, expectedEl.outerHTML)
          }

        }
      }
    }
  }

  test("renders two text nodes") {
    val span_tag = span(text1, text2)
    span_tag.renderInto(mainApp()).surround {
      IO {
        val expectedEl = document.createElement("span")
        expectedEl.textContent = text1 + text2
        val actual = dom.document.querySelector("#app > span")
        assert(actual != null, "querySelector returned null check if the query is correct")
        assertEquals(actual.outerHTML, expectedEl.outerHTML)
      }.flatMap { _ =>

        val article_tag = articleTag(text1, text2)
        article_tag.renderInto(mainApp()).surround {
          IO {
            val expectedEl = document.createElement("article")
            expectedEl.textContent = text1 + text2
            val actual = dom.document.querySelector("#app > article")
            assert(actual != null, "querySelector returned null check if the query is correct")
            assertEquals(actual.outerHTML, expectedEl.outerHTML)
          }
        }
      }
    }
  }
  test("renders nested elements") {
    val span_tag = span(text1, span(text2))
    span_tag.renderInto(mainApp()).surround {
      IO {
        val expectedEl = document.createElement("span")
        expectedEl.textContent = text1 + text2
        val actual = dom.document.querySelector("#app > span")
        assert(actual != null, "querySelector returned null check if the query is correct")
        assertEquals(actual.outerHTML, expectedEl.outerHTML)
      }.flatMap { _ =>

        val article_tag = articleTag(text1, span(text2))
        article_tag.renderInto(mainApp()).surround {
          IO {
            val expectedEl = document.createElement("article")
            expectedEl.textContent = text1
            val spanEl = document.createElement("span")
            spanEl.textContent = text2
            expectedEl.appendChild(spanEl)
            val actual = dom.document.querySelector("#app > article")
            assert(actual != null, "querySelector returned null check if the query is correct")
            assertEquals(actual.outerHTML, expectedEl.outerHTML)
          }
        }
      }

      // test div > span, p, p
      val div_tag = div(span(text1), p(text2), p(text3))
      div_tag
        .renderInto(mainApp())
        .surround {
          IO {
            val expectedEl = document.createElement("div")
            val spanEl = document.createElement("span")
            spanEl.textContent = text1
            expectedEl.appendChild(spanEl)
            val pEl = document.createElement("p")
            pEl.textContent = text2
            expectedEl.appendChild(pEl)
            val pEl2 = document.createElement("p")
            pEl2.textContent = text3
            expectedEl.appendChild(pEl2)
            val actual = dom.document.querySelector("#app > div")
            assert(actual != null, "querySelector returned null check if the query is correct")
            assertEquals(actual.outerHTML, expectedEl.outerHTML)
          }
        }
        .flatMap { _ =>
          // test "div > span, (p > #text, span, span), hr"
          val div_tag2 = div(span(text1), p(text2, span(text3), span(text3)), hr(""))
          div_tag2.renderInto(mainApp()).surround {
            IO {
              val expectedEl = document.createElement("div")
              val spanEl = document.createElement("span")
              spanEl.textContent = text1
              expectedEl.appendChild(spanEl)
              val pEl = document.createElement("p")
              pEl.textContent = text2
              val spanEl2 = document.createElement("span")
              spanEl2.textContent = text3
              pEl.appendChild(spanEl2)
              val spanEl3 = document.createElement("span")
              spanEl3.textContent = text3
              pEl.appendChild(spanEl3)
              expectedEl.appendChild(pEl)
              val hrEl = document.createElement("hr")
              expectedEl.appendChild(hrEl)
              val actual = dom.document.querySelector("#app > div")
              assert(
                actual != null,
                "querySelector returned null check if the query is correct")
              assertEquals(actual.outerHTML, expectedEl.outerHTML)
            }
          }
        }
    }
  }

  test("renders a comment - Not supported by calico".ignore) {
    // TODO calico doesnt support as on version 0.2.2
  }

  test("renders foreign HTML elements - Not supported by calico".ignore) {
    // TODO calico doesnt support as on version 0.2.2
  }
  test("renders foreign SVG root elements Not supported by calico".ignore) {
    // TODO calico doesnt support as on version 0.2.2
  }
  test("renders foreign SVG sub-elements Not supported by calico".ignore) {
    // TODO calico doesnt support as on version 0.2.2
  }

}
