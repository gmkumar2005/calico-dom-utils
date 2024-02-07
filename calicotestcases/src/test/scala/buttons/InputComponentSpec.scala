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

  test("update span after dispatch input") {
    input_span_component.mountInto(rootElement).surround {
      IO.cede.replicateA_(3) *>
        IO {
          val inputChangeEvent = new dom.InputEvent(
            "input",
            new InputEventInit {
              data = "Ram"
            })
          val actualInput =
            document.querySelector("#app > div > input").asInstanceOf[dom.html.Input]
          actualInput.value = "Ram"
          actualInput.addEventListener[dom.InputEvent](
            "input",
            listener = (ev: dom.InputEvent) => {
              println(s"input event fired with data: ${ev.data}") // Printing after assertion
            })
          actualInput.dispatchEvent(inputChangeEvent)
        } *> IO.cede.replicateA_(10) *> IO {
          val actualSpan =
            document.querySelector("#app > div > span").asInstanceOf[dom.html.Span]
          actualSpan.textContent should equal(" Hello, RAM")
        }
    }
  }
}
