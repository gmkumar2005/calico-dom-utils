package buttons

import cats.effect.IO
import cats.effect.Resource
import domutils.CalicoSuite
import fs2.dom.Element
import calico.*
import calico.html.io.{*, given}

class Buttons extends CalicoSuite {

  test("Count clicks on button") {
    val clickme_button: Resource[IO, Element[IO]] =
      button("Click me!", styleAttr := "color: red;")

  }
}
