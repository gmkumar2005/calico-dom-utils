package domutils

import scala.util.Random

trait Utils {

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
