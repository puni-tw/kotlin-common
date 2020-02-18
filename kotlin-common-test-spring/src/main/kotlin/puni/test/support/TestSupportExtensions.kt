package puni.test.support

import feign.FeignException
import io.kotlintest.Failures
import io.kotlintest.shouldThrow
import org.springframework.http.HttpStatus
import puni.data.dto.ApiErrorResponse
import puni.exception.BusinessException
import puni.exception.ErrorCode
import puni.extension.jackson.jsonStringToObject

/**
 * @author leo
 */
fun errCodeMatches(errorCode: ErrorCode, block: () -> Unit) {
  val businessException = shouldThrow<BusinessException> { block() }
  if (businessException.code != errorCode) {
    throw Failures.failure("Expected errorCode $errorCode but got ${businessException.code}")
  }
}

fun apiErrCodeMatches(errorCode: ErrorCode, block: () -> Unit) {
  val feignException = shouldThrow<FeignException> { block() }
  try {
    val json = feignException.contentUTF8()
    val apiErrorResponse = json.jsonStringToObject(ApiErrorResponse::class)
    if (apiErrorResponse.code != errorCode.code) {
      throw Failures.failure("Expected errorCode $errorCode(${errorCode.code}) but got ${apiErrorResponse.code}\r\nresponseBody=\r\n$json")
    }
  } catch (e: Exception) {
    throw Failures.failure("Error handling feignException, status=${feignException.status()}", e)
  }
}

fun httpStatusMatches(httpStatus: HttpStatus, block: () -> Unit) {
  val feignException = shouldThrow<FeignException> { block() }
  try {
    val responseBody = feignException.contentUTF8()
    if (feignException.status() != httpStatus.value()) {
      throw Failures.failure(
        """Expected httpStatus $httpStatus(${httpStatus.value()}) but got ${feignException.status()}
        |responseBody=
        |$responseBody""".trimMargin()
      )
    }
  } catch (e: Exception) {
    throw Failures.failure("Error handling feignException, status=${feignException.status()}", e)
  }
}
