package basic

import calico.html.io.{*, given}
import calico.syntax.*
import cats.effect.IO
import cats.syntax.all.*
import domutils.CalicoSuite
import domutils.Utils.randomString
import munit.CatsEffectSuite
import org.scalajs.dom
import org.scalajs.dom.document

class ReflectedAttrSuite extends CalicoSuite {
  test("sets reflected attrs") {
    val expectedRel = randomString("rel_")
    val expectedHref = randomString("href_")
    val expectedAlt = randomString("alt_")

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
}
