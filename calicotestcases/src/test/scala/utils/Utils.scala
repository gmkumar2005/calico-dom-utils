package utils

import cats.Monad
import cats.effect.Resource
import fs2.dom.Dom
import fs2.dom.Node

import scala.util.Random

trait Utils {

  extension (selectElement: org.scalajs.dom.html.Select) {
    def optionsList: List[String] = {
      val options =
        for (i <- 0 until selectElement.options.length) yield selectElement.options(i).value
      options.toList
    }
  }

  extension (element: org.scalajs.dom.Element) {
    def textIgnoreChildren: String = {
      val childNodes = element.childNodes
      val textNodes =
        for (i <- 0 until childNodes.length
          if childNodes(i).nodeType == org.scalajs.dom.Node.TEXT_NODE)
          yield childNodes(i).textContent
      textNodes.mkString
    }
  }

  extension [F[_]](componentUnderTest: Resource[F, Node[F]])
    def mountInto(rootElement: F[Node[F]])(using Monad[F], Dom[F]): Resource[F, Unit] = {
      Resource
        .eval(rootElement)
        .flatMap(root =>
          componentUnderTest.flatMap(e =>
            Resource.make(root.appendChild(e))(_ => root.removeChild(e))))
    }

  def randomString(prefix: String = "", length: Int = 5): String = {
    prefix + Random.nextString(length)
  }

  def randomUtf8(prefix: String = "", length: Int = 5): String = {
    val utf8Chars = (' ' to '~') ++ ('\u00A0' to '\u00FF') // UTF-8 characters range
    val randomUtf8String =
      (1 to length).map(_ => utf8Chars(Random.nextInt(utf8Chars.size))).mkString
    prefix + randomUtf8String
  }

  def randomAlphaNumeric(prefix: String = "", length: Int = 5): String = {
    val alphanumericChars =
      ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9') // alphanumeric characters range
    val randomAlphanumericString =
      (1 to length).map(_ => alphanumericChars(Random.nextInt(alphanumericChars.size))).mkString
    prefix + randomAlphanumericString
  }
}
object Utils extends Utils
