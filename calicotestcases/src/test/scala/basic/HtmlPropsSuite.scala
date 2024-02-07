package basic

import calico.html.io.*
import calico.html.io.given
import calico.syntax.*
import cats.effect.IO
import cats.effect.Resource
import cats.syntax.all.*
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalatest.matchers.should.Matchers.equal
import org.scalatest.matchers.should.Matchers.should
import org.scalatest.matchers.should.Matchers.shouldBe
import utils.CalicoSpec

class HtmlPropsSuite extends CalicoSpec {

  test("sets props") {
    val checked_input =
      input("").flatTap(_.modify(typ := "checkbox")).flatTap(_.modify(checked := false))
    checked_input
      .mountInto(mainApp())
      .surround {
        IO {
          val expectedEl = document.createElement("input").asInstanceOf[dom.html.Input]
          expectedEl.checked = false
          expectedEl.setAttribute("type", "checkbox")
          val actual = dom.document.querySelector("#app > input").asInstanceOf[dom.html.Input]
          assert(actual != null, "querySelector returned null check if the query is correct")
          actual.outerHTML should equal(expectedEl.outerHTML)
          actual.checked shouldBe expectedEl.checked
        }
      }
      .flatMap { _ =>
        val value_of_input = input("").flatTap(_.modify(calico.html.io.value := "yolo"))
        value_of_input.mountInto(mainApp()).surround {
          IO {
            val expectedEl = document.createElement("input").asInstanceOf[dom.html.Input]
            expectedEl.value = "yolo"
            val actual = dom.document.querySelector("#app > input").asInstanceOf[dom.html.Input]
            assert(actual != null, "querySelector returned null check if the query is correct")
            actual.outerHTML should equal(expectedEl.outerHTML)
            actual.value shouldBe expectedEl.value
          }
        }

        val true_option =
          option("true")
            .flatTap(_.modify(selected := true))
            .flatTap(_.modify(calico.html.io.value := "123"))
        true_option.mountInto(mainApp()).surround {
          IO {
            val expectedEl = document.createElement("option").asInstanceOf[dom.html.Option]
            expectedEl.selected = true
            expectedEl.textContent = "true"
            expectedEl.value = "123"
            val actual =
              dom.document.querySelector("#app > option").asInstanceOf[dom.html.Option]
            assert(actual != null, "querySelector returned null check if the query is correct")
            actual.outerHTML should equal(expectedEl.outerHTML)
            actual.selected shouldBe expectedEl.selected
            actual.textContent shouldBe expectedEl.textContent
          }
        }
      }
      .flatMap { _ =>

        val false_option =
          option("false")
            .flatTap(_.modify(selected := false))
            .flatTap(_.modify(calico.html.io.value := "123"))
        false_option.mountInto(mainApp()).surround {
          IO {
            val expectedEl = document.createElement("option").asInstanceOf[dom.html.Option]
            expectedEl.selected = false
            expectedEl.textContent = "false"
            expectedEl.value = "123"
            val actual =
              dom.document.querySelector("#app > option").asInstanceOf[dom.html.Option]
            assert(actual != null, "querySelector returned null check if the query is correct")
            actual.outerHTML should equal(expectedEl.outerHTML)
            actual.selected shouldBe expectedEl.selected
            actual.textContent should equal(expectedEl.textContent)
          }
        }
      }
      .flatMap { _ =>
        val div_input =
          div(
            input("").flatTap(_.modify(typ := "checkbox")).flatTap(_.modify(checked := false)))
        div_input.mountInto(mainApp()).surround {
          IO {
            val expectedEl = document.createElement("div")
            val inputEl = document.createElement("input").asInstanceOf[dom.html.Input]
            inputEl.checked = false
            inputEl.setAttribute("type", "checkbox")
            expectedEl.appendChild(inputEl)
            val actual = dom.document.querySelector("#app > div")
            assert(actual != null, "querySelector returned null check if the query is correct")
            actual.outerHTML should equal(expectedEl.outerHTML)
            val actualInput =
              dom.document.querySelector("#app > div > input").asInstanceOf[dom.html.Input]
            actualInput.outerHTML should equal(inputEl.outerHTML)
            actualInput.checked shouldBe inputEl.checked
          }
        }
      }

  }
}
