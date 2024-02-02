package basic

import cats.effect.IO
import org.scalajs.dom
import utils.CalicoSpec

/**
 * Sanity checks on the testing environment. This does not use this library at all.
 */
class DomEnvSpec extends CalicoSpec {

  test("renders elements with attributes") {
    IO {
      val spanId = randomString("spanId_")
      val span = dom.document.createElement("span")
      span.setAttribute("id", spanId)
      dom.document.body.appendChild(span)
      spanId should equal(span.id)
    }
  }
  test("handles click events") {
    IO {
      var callbackCount = 0

      val testEvent: dom.MouseEvent => Unit = (ev: dom.MouseEvent) => {
        callbackCount += 1
      }
      val div = dom.document.createElement("div").asInstanceOf[dom.html.Div]
      val div2 = dom.document.createElement("div").asInstanceOf[dom.html.Div]
      val span = dom.document.createElement("span").asInstanceOf[dom.html.Span]
      div.addEventListener[dom.MouseEvent]("click", listener = testEvent)
      div.appendChild(span)
      dom.document.body.appendChild(div)
      dom.document.body.appendChild(div2)
      withClue("Click event count should be 1") {
        span.click()
        1 should equal(callbackCount)
      }
      withClue("Click event should bubble up") {
        span.click()
        2 should equal(callbackCount)
      }
      withClue("Click event should not be counted on unrelated div") {
        div2.click()
        2 should equal(callbackCount)
      }

    }
  }

}
