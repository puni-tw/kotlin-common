package puni.log

import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec

class LoggableTest : StringSpec({
  "should able to access log" {
    class Foo : Loggable
    Foo().LOGGER shouldNotBe null
  }
})
