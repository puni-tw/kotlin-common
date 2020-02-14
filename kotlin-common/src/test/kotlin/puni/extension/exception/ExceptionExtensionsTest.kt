package puni.extension.exception

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import puni.test.support.errCodeMatches
import puni.test.support.errMessageMatches

class ExceptionExtensionsTest : StringSpec({
  "should able to catch errorCode" {
    errCodeMatches(TestErrorCode.ERROR) {
      1.errWhen(TestErrorCode.ERROR) { it == 1 }
    }
  }
  "should pass expression" {
    1.errWhen(TestErrorCode.ERROR) { it != 1 }
  }
  "should able to catch errWhenNull" {
    errCodeMatches(TestErrorCode.ERROR) {
      val foo: Int? = null
      foo.errWhenNull(TestErrorCode.ERROR)
    }
    "bar".errWhenNull(TestErrorCode.ERROR)
  }
  "should able to catch exception" {
    1.errWhenException(TestErrorCode.ERROR) {
      it.toString()
    } shouldBe "1"
    errCodeMatches(TestErrorCode.ERROR) {
      1.errWhenException(TestErrorCode.ERROR) {
        throw Exception()
      }
    }
  }
  "should able to handle with message" {
    errMessageMatches("1+2=3") {
      1.errWhen(TestErrorCode.ERROR, "{}+{}={}", 1, 2, 3) { it == 1 }
    }
    errMessageMatches("foo") {
      1.errWhen(TestErrorCode.ERROR, "foo") { it == 1 }
    }
  }
})
