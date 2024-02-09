package basic

import calico.*
import calico.html.io.*
import calico.html.io.div
import calico.html.io.given
import cats.Monad
import cats.effect.IO
import cats.effect.Resource
import cats.effect.testing.scalatest.AsyncIOSpec
import com.raquo.domtestutils.matching.ExpectedNode
import fs2.dom.Dom
import fs2.dom.Element
import fs2.dom.HtmlDivElement
import fs2.dom.HtmlSpanElement
import fs2.dom.Node
import fs2.dom.Window
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalatest.funsuite.AsyncFunSuite
import org.scalatest.matchers.should.Matchers.should
import utils.CalicoMountSpec

import scala.language.implicitConversions

class ExpectSpec extends AsyncFunSuite with AsyncIOSpec with CalicoMountSpec {
  // Prepare the DOM
  val appDiv: dom.Element = document.createElement("div")
  appDiv.id = "root"
  document.body.appendChild(appDiv)
  var containerNode1: dom.Element = appDiv
  override def rootNode: dom.Node = Option(containerNode1).map(_.firstChild).orNull
  val rootElementId: String = "root"
  val window: Window[IO] = Window[IO]
  val rootElement: IO[Node[IO]] = window.document.getElementById(rootElementId).map(_.get)
  def mainApp(): IO[Node[IO]] = rootElement
  extension [F[_]](componentUnderTest: Resource[F, Node[F]])
    /**
     * Combines the component under test with a root element and mounts the component into the
     * root element.
     */
    def mountInto(rootElement: F[Node[F]])(using Monad[F], Dom[F]): Resource[F, Unit] = {
      Resource
        .eval(rootElement)
        .flatMap(root =>
          componentUnderTest.flatMap(e =>
            Resource.make(root.appendChild(e))(_ => root.removeChild(e))))
    }

  implicit def makeDivTestable(tag: Resource[IO, HtmlDivElement[IO]]): IO[ExpectedNode] = {
    makeElementTestable(tag)
  }
  implicit def makeElementTestable(tag: Resource[IO, Element[IO]]): IO[ExpectedNode] = {
    tag.use {
      case div: HtmlDivElement[IO] =>
        val expectedDiv = ExpectedNode.element("div")
        val expectedTxt = ExpectedNode.textNode
        expectedDiv.addExpectedChild(expectedTxt)
        IO.pure(expectedDiv)
      case span: HtmlSpanElement[IO] =>
        val expectedSpan = ExpectedNode.element("span")
        val expectedTxt = ExpectedNode.textNode
        expectedSpan.addExpectedChild(expectedTxt)
        IO.pure(expectedSpan)
      case _ =>
        val expectedDiv = ExpectedNode.element("div")
        val expectedTxt = ExpectedNode.textNode
        expectedDiv.addExpectedChild(expectedTxt)
        IO.pure(expectedDiv)
    }
  }

  implicit def makeSpanTestable(tag: Resource[IO, HtmlSpanElement[IO]]): IO[ExpectedNode] = {
    makeElementTestable(tag)
  }

  def expectNode(expected: IO[ExpectedNode]): IO[Unit] = {
    expected.map { expected =>
      println(s"expected: $expected")
      expectNode(expected)
    }
  }

  test("renders empty div") {
    val empty_div: Resource[IO, HtmlDivElement[IO]] = div("")
    empty_div.mountInto(mainApp()).surround {
      expectNode(empty_div)
    }
  }

  test("renders empty elements") {
    val empty_element: Resource[IO, Element[IO]] = div("")
    val empty_div: Resource[IO, HtmlDivElement[IO]] = div("")
    val empty_span: Resource[IO, HtmlSpanElement[IO]] = span("")

    empty_element.mountInto(mainApp()).surround {
      expectNode(empty_div)
    } *>
      empty_div.mountInto(mainApp()).surround {
        expectNode(empty_div)
      } *>
      empty_span.mountInto(mainApp()).surround {
        expectNode(empty_span)
      }
  }

}
