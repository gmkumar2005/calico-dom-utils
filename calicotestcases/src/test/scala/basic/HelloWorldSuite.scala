package basic

import calico.*
import calico.html.io.{*, given}
import calico.syntax.*
import cats.effect.IO
import cats.effect.kernel.Resource
import fs2.dom.*
import munit.CatsEffectSuite
import org.scalajs.dom
import org.scalajs.dom.document

class HelloWorldSuite extends CatsEffectSuite {

  val args: List[String] = List.empty

  test("render should return expected HTML element") {
    val appDiv = document.createElement("div")
    appDiv.id = "app"
    document.body.appendChild(appDiv)

    val rootElementId: String = "app"
    val window: Window[IO] = Window[IO]
    val hello_component: Resource[IO, fs2.dom.HtmlElement[IO]] =
      div(i("hello"), " ", b("world"))

    val rootElement: IO[fs2.dom.Element[IO]] =
      window.document.getElementById(rootElementId).map(_.get)

    rootElement.flatMap(hello_component.renderInto(_).surround {
      IO {
        val documentContents = dom.document.documentElement.innerHTML
//        println(s"documentContents: $documentContents")
        assertEquals(
          documentContents.contains("hello"),
          true,
          s"Document does not contain 'hello' actual document contents: $documentContents"
        )
      }
    })

  }

}
