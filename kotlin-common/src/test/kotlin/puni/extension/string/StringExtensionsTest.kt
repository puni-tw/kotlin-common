package puni.extension.string

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

/**
 * @author leo
 */
class StringExtensionsTest : StringSpec({
  "should able to replace {} in string  to arg" {
    "this is a {}".replaceByArgs("book") shouldBe "this is a book"
    "this is a {} {}".replaceByArgs("book") shouldBe "this is a book {}"
    "{} {} {}".replaceByArgs("I", "am", "Groot") shouldBe "I am Groot"
    "this is an apple".replaceByArgs("book") shouldBe "this is an apple"
  }
})
