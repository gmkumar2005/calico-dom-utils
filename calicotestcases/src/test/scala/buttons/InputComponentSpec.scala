package buttons

import calico.*
import calico.html.io.*
import calico.html.io.given
import cats.effect.IO
import cats.effect.kernel.Resource
import fs2.concurrent.SignallingRef
import fs2.dom.*
import org.scalajs.dom
import org.scalajs.dom.InputEventInit
import org.scalajs.dom.document
import utils.CalicoSpec

class InputComponentSpec extends CalicoSpec {
  val input_span_component: Resource[IO, HtmlDivElement[IO]] =
    SignallingRef[IO].of("world").toResource.flatMap { name =>
      div(
        label("Your name: "),
        input.withSelf { self =>
          (
            placeholder := "Enter your name here",
            // here, input events are run through the given Pipe
            // this starts background fibers within the lifecycle of the <input> element
            onInput --> (_.foreach(_ => self.value.get.flatMap(name.set)))
          )
        },
        span(
          " Hello, ",
          // here, a Signal is rendered into the HTML
          // this starts background fibers within the life cycle of the <span> element
          name.map(_.toUpperCase)
        )
      )
    }

  test("renders label elements and input elements dispatch events") {
    input_span_component.mountInto(rootElement).surround {
      IO {
        val expectedEl = document.createElement("div")
        val expectedLabel = document.createElement("label")
        expectedLabel.textContent = "Your name: "
        expectedEl.appendChild(expectedLabel)
        val actualLabel = dom.document.querySelector("#app > div > label")
        assert(
          actualLabel != null,
          "querySelector returned null. Check if the query is correct")
        actualLabel.outerHTML should equal(expectedLabel.outerHTML)
        val actualInput =
          document.querySelector("#app > div > input").asInstanceOf[dom.html.Input]
        val actualSpan = document.querySelector("#app > div > span").asInstanceOf[dom.html.Span]
        actualSpan.textContent should equal(" Hello, WORLD")
        actualInput.addEventListener[dom.InputEvent](
          "input",
          (ev: dom.InputEvent) => {
            println(s"change event fired ${ev.data}") // working as expected
          })
        withClue("Span value should be 'Hello, RAM' when input value updated to 'Ram'") {
          actualInput.value = "Ram"
          // Manually dispatch an InputEvent
          val inputChangeEvent = new dom.InputEvent(
            "input",
            new InputEventInit {
              bubbles = true
              cancelable = true
              data = "Ram"
            })

          actualInput.dispatchEvent(inputChangeEvent)
          actualSpan.textContent should equal(" Hello, RAM")
        }

      }
    }
  }
}
