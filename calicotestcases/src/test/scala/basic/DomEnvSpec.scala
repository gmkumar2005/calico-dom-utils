package basic

import cats.effect.IO
import org.scalajs.dom
import org.scalatest.matchers.should.Matchers.equal
import org.scalatest.matchers.should.Matchers.should
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

  test("change events are  triggered by setting input value directly") {
    IO {
      var callbackCount = 0
      val testEvent: dom.Event => Unit = (ev: dom.Event) => {
        callbackCount += 1
      }
      val input = dom.document.createElement("input").asInstanceOf[dom.html.Input]
      input.addEventListener[dom.Event]("change", listener = testEvent)
      dom.document.body.appendChild(input)
      withClue("Change event count should be one") {
        input.value = "one"
        // Manually trigger the 'change' event
        input.dispatchEvent(new dom.Event("change"))
        1 should equal(callbackCount)
        input.value should equal("one")
      }
      withClue("Change event count should be two") {
        input.value = "two"
        // Manually trigger the 'change' event
        input.dispatchEvent(new dom.Event("change"))
        2 should equal(callbackCount)
        input.value should equal("two")
      }
    }
  }
  test("change events are  triggered when span content is changed") {
    IO {
      var callbackCount = 0
      val testEvent: dom.Event => Unit = (ev: dom.Event) => {
        callbackCount += 1
      }
      val span = dom.document.createElement("span").asInstanceOf[dom.html.Span]
      span.addEventListener[dom.Event]("change", listener = testEvent)
      dom.document.body.appendChild(span)
      withClue("Change event count should be one") {
        span.textContent = "one"
        // Manually trigger the 'change' event
        span.dispatchEvent(new dom.Event("change"))
        1 should equal(callbackCount)
        span.textContent should equal("one")
      }
      withClue("Change event count should be two") {
        span.textContent = "two"
        // Manually trigger the 'change' event
        span.dispatchEvent(new dom.Event("change"))
        2 should equal(callbackCount)
        span.textContent should equal("two")
      }
    }
  }

  test("change in input value should update textContent of a span") {
    IO {
      val input = dom.document.createElement("input").asInstanceOf[dom.html.Input]
      val span = dom.document.createElement("span").asInstanceOf[dom.html.Span]
      dom.document.body.appendChild(input)
      dom.document.body.appendChild(span)
      // attach event listner to input which will update span textContent
      input.addEventListener[dom.Event](
        "change",
        (ev: dom.Event) => {
          span.textContent = input.value.toUpperCase
        })

      input.value = "one"
      input.dispatchEvent(new dom.Event("change"))
      span.textContent should equal("ONE")
      input.value = "two"
      input.dispatchEvent(new dom.Event("change"))
      span.textContent should equal("TWO")
    }
  }
  test("change in input value should update textContent of a span using input event") {
    IO {
      val input = dom.document.createElement("input").asInstanceOf[dom.html.Input]
      val span = dom.document.createElement("span").asInstanceOf[dom.html.Span]
      dom.document.body.appendChild(input)
      dom.document.body.appendChild(span)
      // attach event listner to input which will update span textContent
      input.addEventListener[dom.Event](
        "input",
        (ev: dom.Event) => {
          span.textContent = input.value.toUpperCase
        })

      input.value = "one"
      input.dispatchEvent(new dom.Event("input"))
      span.textContent should equal("ONE")
      input.value = "two"
      input.dispatchEvent(new dom.Event("input"))
      span.textContent should equal("TWO")
    }
  }
}
