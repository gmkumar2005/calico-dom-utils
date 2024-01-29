package basic

import cats.effect.IO
import domutils.Utils.*
import munit.Assertions.assertEquals
import munit.CatsEffectSuite
import org.scalajs.dom

/**
 * Sanity checks on the testing environment. This does not use this library at all.
 */
class DomEnvSuite extends CatsEffectSuite {

  test("renders elements with attributes") {
    IO {
      val spanId = randomString("spanId_")
      val span = dom.document.createElement("span")
      span.setAttribute("id", spanId)
      dom.document.body.appendChild(span)

      assertEquals(
        span.id,
        spanId,
        s"span.id should be $spanId, but was ${span.id}"
      )
    }
  }
  test("handles click events") {
    IO {
      var callbackCount = 0
      def testEvent(ev: dom.MouseEvent): Unit = {
        callbackCount += 1
      }
      val div = dom.document.createElement("div").asInstanceOf[dom.html.Div]
      val div2 = dom.document.createElement("div").asInstanceOf[dom.html.Div]
      val span = dom.document.createElement("span").asInstanceOf[dom.html.Span]
      div.addEventListener[dom.MouseEvent]("click", testEvent)
      div.appendChild(span)
      dom.document.body.appendChild(div)
      dom.document.body.appendChild(div2)
      // Direct hit
      div.click()
      assertEquals(clue(1), clue(callbackCount), clue("Click count should be 1"))
      // Click event should bubble up
      span.click()
      assertEquals(clue(2), clue(callbackCount), clue("Click count should be 2"))
      // Click should not be counted on unrelated div
      div2.click()
      assertEquals(
        clue(2),
        clue(callbackCount),
        clue("Click should not be counted on unrelated div and should be 2"))
    }
  }

}
