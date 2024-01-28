//package basic
//
//import calico.*
//import calico.html.io.given
//import cats.Monad
//import cats.effect.{IO, Resource}
//import fs2.dom.{Dom, Element, Window}
//import munit.Assertions.{assertEquals, fail}
//import munit.{Compare, Location}
//import org.scalajs.dom
//import org.scalajs.dom.document
////import org.scalajs.dom
//
//trait CalicoSuite {
//  val appDiv: dom.Element = document.createElement("div")
//  appDiv.id = "app"
//  document.body.appendChild(appDiv)
//
//  val rootElementId: String = "app"
//  val window: Window[IO] = Window[IO]
//  val rootElement: IO[Element[IO]] = window.document.getElementById(rootElementId).map(_.get)
//
//  extension [F[_]](componentUnderTest: Resource[F, Element[F]])
//    def mountInto(rootElement: F[Element[F]])(using Monad[F], Dom[F]): Resource[F, Element[F]] = {
//      for {
//        root <- Resource.eval(rootElement)
//        _ <- componentUnderTest.flatMap(e => Resource.make(root.appendChild(e))(_ => root.removeChild(e)))
//      } yield root
//    }
//
//  def assertQuery(queryString: String, expected: Int, clue: => Any = "values are not the same")(implicit
//      loc: Location
//  ): Unit = {
//
//    val element = org.scalajs.dom.document.querySelectorAll(queryString)
//    element match
//      case nodeList: org.scalajs.dom.NodeList[org.scalajs.dom.Element] =>
//        assertEquals(nodeList.length, expected, clue)
//      case null =>
//        fail("not a node list")
//
//  }
//}
//
