package basic

import calico.*
import calico.html.io.*
import calico.html.io.given
import cats.effect.IO
import cats.effect.Resource
import fs2.dom.Element
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalatest.matchers.should.Matchers.*
import utils.CalicoSpec

class ElementsSpec extends CalicoSpec {
  private val text1 = randomString("text1_")
  private val text2 = randomString("text2_")
  private val text3 = randomString("text3_")

  test("renders empty elements") {
//    val my_div: Resource[IO, org.scalajs.dom.Element] =
//      IO(org.scalajs.dom.document.createElement("div")).toResource

    val empty_div: Resource[IO, Element[IO]] = div("")
    empty_div.mountInto(mainApp()).surround {
      IO {
        val expectedEl = document.createElement("div")
        val actual = dom.document.querySelector("#app > div")
        assert(actual != null, "querySelector returned null check if the query is correct")
        actual.outerHTML should equal(expectedEl.outerHTML)
      }
    } *> {
      val empty_span: Resource[IO, Element[IO]] = span("")
      empty_span.mountInto(mainApp()).surround {
        IO {
          val expectedEl = document.createElement("span")
          val actual = dom.document.querySelector("#app > span")
          assert(actual != null, "querySelector returned null check if the query is correct")
          actual.outerHTML should equal(expectedEl.outerHTML)
        }
      }
    } *> {
      val empty_p: Resource[IO, Element[IO]] = p("")
      empty_p.mountInto(mainApp()).surround {
        IO {
          val expectedEl = document.createElement("p")
          val actual = dom.document.querySelector("#app > p")
          assert(actual != null, "querySelector returned null check if the query is correct")
          actual.outerHTML should equal(expectedEl.outerHTML)
        }
      }
    } *> {
      val empty_hr: Resource[IO, Element[IO]] = hr("")
      empty_hr.mountInto(mainApp()).surround {
        IO {
          val expectedEl = document.createElement("hr")
          val actual = dom.document.querySelector("#app > hr")
          assert(actual != null, "querySelector returned null check if the query is correct")
          actual.outerHTML should equal(expectedEl.outerHTML)
        }
      }
    }

  }

  test("renders elements with text Content") {
    val span_tag = span(text1)
    span_tag.mountInto(mainApp()).surround {
      IO {
        val expectedEl = document.createElement("span")
        expectedEl.textContent = text1
        val actual = dom.document.querySelector("#app > span")
        assert(actual != null, "querySelector returned null check if the query is correct")
        actual.outerHTML should equal(expectedEl.outerHTML)
      } *> {
        val article_tag = articleTag(text1)
        article_tag.mountInto(mainApp()).surround {
          IO {
            val expectedEl = document.createElement("article")
            expectedEl.textContent = text1
            val actual = dom.document.querySelector("#app > article")
            assert(actual != null, "querySelector returned null check if the query is correct")
            actual.outerHTML should equal(expectedEl.outerHTML)
          }

        }
      }
    }
  }

  test("renders two text nodes") {
    val span_tag = span(text1, text2)
    span_tag.mountInto(mainApp()).surround {
      IO {
        val expectedEl = document.createElement("span")
        expectedEl.textContent = text1 + text2
        val actual = dom.document.querySelector("#app > span")
        assert(actual != null, "querySelector returned null check if the query is correct")
        actual.outerHTML should equal(expectedEl.outerHTML)
      }.flatMap { _ =>

        val article_tag = articleTag(text1, text2)
        article_tag.mountInto(mainApp()).surround {
          IO {
            val expectedEl = document.createElement("article")
            expectedEl.textContent = text1 + text2
            val actual = dom.document.querySelector("#app > article")
            assert(actual != null, "querySelector returned null check if the query is correct")
            actual.outerHTML should equal(expectedEl.outerHTML)
          }
        }
      }
    }
  }
  test("renders nested elements") {
    val span_tag = span(text1, span(text2))
    span_tag.mountInto(mainApp()).surround {
      IO {
        val expectedEl = document.createElement("span")
        expectedEl.textContent = text1 + text2
        val actual = dom.document.querySelector("#app > span")
        assert(actual != null, "querySelector returned null check if the query is correct")
        actual.outerHTML should equal(expectedEl.outerHTML)
      }.flatMap { _ =>

        val article_tag = articleTag(text1, span(text2))
        article_tag.mountInto(mainApp()).surround {
          IO {
            val expectedEl = document.createElement("article")
            expectedEl.textContent = text1
            val spanEl = document.createElement("span")
            spanEl.textContent = text2
            expectedEl.appendChild(spanEl)
            val actual = dom.document.querySelector("#app > article")
            assert(actual != null, "querySelector returned null check if the query is correct")
            actual.outerHTML should equal(expectedEl.outerHTML)
          }
        }
      }

      // test div > span, p, p
      val div_tag = div(span(text1), p(text2), p(text3))
      div_tag
        .mountInto(mainApp())
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
            actual.outerHTML should equal(expectedEl.outerHTML)
          }
        }
        .flatMap { _ =>
          // test "div > span, (p > #text, span, span), hr"
          val div_tag2 = div(span(text1), p(text2, span(text3), span(text3)), hr(""))
          div_tag2.mountInto(mainApp()).surround {
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
              actual.outerHTML should equal(expectedEl.outerHTML)
            }
          }
        }
    }
  }

//  test("renders a comment - Not supported by calico".ignore) {
//    // TODO calico doesnt support as on version 0.2.2
//  }
//
//  test("renders foreign HTML elements - Not supported by calico".ignore) {
//    // TODO calico doesnt support as on version 0.2.2
//  }
//  test("renders foreign SVG root elements Not supported by calico".ignore) {
//    // TODO calico doesnt support as on version 0.2.2
//  }
//  test("renders foreign SVG sub-elements Not supported by calico".ignore) {
//    // TODO calico doesnt support as on version 0.2.2
//  }

}
