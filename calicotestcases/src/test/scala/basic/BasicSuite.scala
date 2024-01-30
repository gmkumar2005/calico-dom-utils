package basic

import calico.*
import calico.syntax.*
import calico.html.io.*
import calico.html.io.given
import cats.effect.IO
import cats.effect.Resource
import domutils.CalicoSuite
import fs2.dom.Element
import fs2.dom.Node
import munit.CatsEffectSuite
import munit.catseffect.IOFixture
import org.scalajs.dom
import org.scalajs.dom.document

class BasicSuite extends CalicoSuite {

  /**
   * The `mainApp` is a test fixture in Scala. A fixture is a fixed state of a set of objects
   * used as a baseline for running tests. The purpose of a test fixture is to ensure that there
   * is a well-known and fixed environment in which tests are run so that results are
   * repeatable. The `mainApp` is an IOFixture which is a type of fixture provided by the
   * munit-cats-effect library. IOFixture is used for managing resources that have a lifecycle,
   * such as opening and closing a database connection, or starting and stopping a server. The
   * IOFixture is shared across all tests in the suite. It is created once and then passed to
   * each test and cleaned up after all tests are run.
   */
  val mainApp: IOFixture[Node[IO]] = ResourceSuiteLocalFixture(
    "main-app",
    Resource.eval(rootElement)
  )

  override def munitFixtures = List(mainApp)

  test("renders empty elements") {
    val empty_div: Resource[IO, Element[IO]] = div("")
    empty_div.renderInto(mainApp()).surround {
      IO {
        val expectedEl = document.createElement("div")
        val actual = dom.document.querySelector("#app > div")
        assert(actual != null, "querySelector returned null check if the query is correct")
        assertEquals(actual.outerHTML, expectedEl.outerHTML)
      }
    } *> {
      val empty_span: Resource[IO, Element[IO]] = span("")
      empty_span.renderInto(mainApp()).surround {
        IO {
          val expectedEl = document.createElement("span")
          val actual = dom.document.querySelector("#app > span")
          assert(actual != null, "querySelector returned null check if the query is correct")
          assertEquals(actual.outerHTML, expectedEl.outerHTML)
        }
      }
    }
  }
}
