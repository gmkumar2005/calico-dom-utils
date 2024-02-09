package utils

import com.raquo.domtestutils.MountOps
import org.scalactic
import org.scalatest.exceptions.StackDepthException
import org.scalatest.exceptions.TestFailedException
import org.scalatest.funsuite.AsyncFunSuite

trait CalicoMountSpec extends AsyncFunSuite with MountOps {

  override def doAssert(
      condition: Boolean,
      message: String
  )(
      implicit prettifier: scalactic.Prettifier,
      pos: scalactic.source.Position
  ): Unit = {
    assert(condition, message)(prettifier, pos, UseDefaultAssertions)
  }

  override def doFail(message: String)(implicit pos: scalactic.source.Position): Nothing = {
    throw new TestFailedException(
      toExceptionFunction(Some(message)),
      None,
      Left(pos),
      None,
      Vector.empty)
  }

  private def toExceptionFunction(
      message: Option[String]): StackDepthException => Option[String] = {
    message match {
      case null => throw new org.scalactic.exceptions.NullArgumentException("message was null")
      case Some(null) =>
        throw new org.scalactic.exceptions.NullArgumentException("message was Some(null)")
      case _ => e => message
    }
  }
}
