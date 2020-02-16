package puni.test.support

import io.kotlintest.TestContext
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import puni.exception.BusinessException
import puni.exception.ErrorCode

/**
 * @author leo
 */
inline fun TestContext.errCodeMatches(errorCode: ErrorCode, block: () -> Unit) {
  shouldThrow<BusinessException> { block() }.code shouldBe errorCode
}
inline fun TestContext.errMessageMatches(message: String, block: () -> Unit) {
  shouldThrow<BusinessException> { block() }.message shouldBe message
}
