package puni.extension.exception

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import puni.exception.CommonErrorCode
import puni.test.support.errCodeMatches
import puni.test.support.errMessageMatches

class ExceptionExtensionsTest : StringSpec({
  "should able to catch errorCode" {
    errCodeMatches(CommonErrorCode.ERROR) {
      1.errWhen(CommonErrorCode.ERROR) { it == 1 }
    }
  }
  "should pass expression" {
    1.errWhen(CommonErrorCode.ERROR) { it != 1 }
  }
  "should able to catch errWhenNull" {
    errCodeMatches(CommonErrorCode.ERROR) {
      val foo: Int? = null
      foo.errWhenNull(CommonErrorCode.ERROR)
    }
    "bar".errWhenNull(CommonErrorCode.ERROR)
  }
  "should able to catch exception" {
    1.errWhenException(CommonErrorCode.ERROR) {
      it.toString()
    } shouldBe "1"
    errCodeMatches(CommonErrorCode.ERROR) {
      1.errWhenException(CommonErrorCode.ERROR) {
        throw Exception()
      }
    }
  }
  "should able to handle with message" {
    errMessageMatches("1+2=3") {
      1.errWhen(CommonErrorCode.ERROR, "{}+{}={}", 1, 2, 3) { it == 1 }
    }
    errMessageMatches("foo") {
      1.errWhen(CommonErrorCode.ERROR, "foo") { it == 1 }
    }
  }
  "should able to get null when error" {
    nullWhenError { Class.forName("not_exist") } shouldBe null
    nullWhenError { "foo" } shouldBe "foo"
  }
})
