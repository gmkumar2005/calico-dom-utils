package buttons

import calico.html.io.*
import calico.html.io.given
import cats.effect.IO
import cats.effect.Resource
import fs2.concurrent.Channel
import fs2.concurrent.SignallingRef
import fs2.dom.HtmlDivElement
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.html.Button
import org.scalajs.dom.html.Element
import org.scalajs.dom.html.Paragraph
import org.scalajs.dom.html.Select
import org.scalatest.matchers.should.Matchers.not
import org.scalatest.matchers.should.Matchers.should
import org.scalatest.matchers.should.Matchers.shouldBe
import utils.CalicoSpec

class CounterSuite extends CalicoSpec {
  def Counter(label: String, initialStep: Int): Resource[IO, HtmlDivElement[IO]] =
    SignallingRef[IO].of(initialStep).product(Channel.unbounded[IO, Int]).toResource.flatMap {
      (step, diff) =>
        val allowedSteps = List(1, 2, 3, 5, 10)
        div(
          p(
            "Step: ",
            select.withSelf { self =>
              (
                allowedSteps.map(step => option(value := step.toString, step.toString)),
                value <-- step.map(_.toString),
                onChange --> {
                  _.evalMap(_ => self.value.get).map(_.toIntOption).unNone.foreach(step.set)
                }
              )
            }
          ),
          p(
            label + ": ",
            b(diff.stream.scanMonoid.map(_.toString).holdOptionResource),
            " ",
            button(
              "-",
              onClick --> {
                _.evalMap(_ => step.get).map(-1 * _).foreach(diff.send(_).void)
              }
            ),
            button(
              "+",
              onClick --> (_.evalMap(_ => step.get).foreach(diff.send(_).void))
            )
          )
        )
    }

  val counter_component: Resource[IO, HtmlDivElement[IO]] = div(
    h1("Let's count!"),
    Counter("Sheep", initialStep = 3)
  )

  test("Step and Sheep should be rendered correctly") {
    counter_component.mountInto(rootElement).surround {
      IO {
        val actualStep =
          document.querySelector("#app > div >div > p").asInstanceOf[Paragraph]
        actualStep should not be null
        actualStep.textIgnoreChildren shouldBe "Step: "
      } *>
        IO {
          val actualSelect =
            document.querySelector("#app > div >div > p > select").asInstanceOf[Select]
          actualSelect should not be null
          actualSelect.value shouldBe "3"
          actualSelect.optionsList shouldBe List("1", "2", "3", "5", "10")
        } *> IO {
          val actualSheep: Paragraph =
            document.querySelector("#app > div >div > p + p").asInstanceOf[Paragraph]
          actualSheep should not be null
          actualSheep.textContent shouldBe "Sheep:  -+"
        }
    }
  }

  test("Sheep count should decrement by 3 when minus button is clicked") {
    counter_component.mountInto(rootElement).surround {
      for {
        _ <- IO.cede.replicateA_(40)
        minusButton = document.querySelector("#app > div div p button").asInstanceOf[Button]
        _ = minusButton should not be null
        actualSheepCount = document
          .querySelector("#app > div >div > p + p > b")
          .asInstanceOf[Element]
        _ = {
          actualSheepCount should not be null
          actualSheepCount.textIgnoreChildren shouldBe "0"
          minusButton.click()
        }
        _ <- IO.cede.replicateA_(40)
        _ = {
          actualSheepCount.textIgnoreChildren shouldBe "-3"
          minusButton.click()
        }
        _ <- IO.cede.replicateA_(40)
        _ = actualSheepCount.textIgnoreChildren shouldBe "-6"
      } yield ()
    }
  }

  test("Sheep count should incremented by 3 when plus button is clicked") {
    counter_component.mountInto(rootElement).surround {
      for {
        _ <- IO.cede.replicateA_(40)
        plusButton = document
          .querySelector("#app > div div p button:nth-child(3)")
          .asInstanceOf[Button]
        _ = plusButton should not be null
        actualSheepCount = document
          .querySelector("#app > div >div > p + p > b")
          .asInstanceOf[Element]
        _ = {
          actualSheepCount should not be null
          actualSheepCount.textIgnoreChildren shouldBe "0"
          plusButton.click()
        }
        _ <- IO.cede.replicateA_(40)
        _ = {
          actualSheepCount.textIgnoreChildren shouldBe "3"
          plusButton.click()
        }
        _ <- IO.cede.replicateA_(40)
        _ = actualSheepCount.textIgnoreChildren shouldBe "6"
      } yield ()
    }
  }
}
