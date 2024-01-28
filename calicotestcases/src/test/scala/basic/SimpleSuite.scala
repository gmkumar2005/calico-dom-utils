package basic

import calico.html.io.{b, div, i}
import calico.*
import calico.html.io.given
import cats.effect.{IO, Resource}
import fs2.dom.Element
import munit.CatsEffectSuite
import org.scalajs.dom
import domutils.CalicoSuite

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
