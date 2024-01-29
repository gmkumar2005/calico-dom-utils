package basic

import calico.*
import calico.html.io.b
import calico.html.io.div
import calico.html.io.given
import calico.html.io.i
import cats.effect.IO
import cats.effect.Resource
import domutils.CalicoSuite
import fs2.dom.Element
import munit.CatsEffectSuite
import org.scalajs.dom

class SimpleSuite extends CalicoSuite {
  test("Simple element") {
    val hello_component: Resource[IO, Element[IO]] = div(i("hello"), " ", b("world"))
    hello_component.mountInto(rootElement).surround {
      IO {
        val documentContents = dom.document.documentElement.innerHTML
        assertEquals(
          documentContents.contains("hello"),
          true,
          s"Document does not contain 'hello' actual document contents: $documentContents"
        )
      }
    }

  }
}
