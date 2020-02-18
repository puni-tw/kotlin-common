package test

import feign.FeignException
import feign.Request
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import org.springframework.http.HttpStatus
import puni.data.dto.ApiErrorResponse
import puni.exception.ApiErrorCode
import puni.exception.BusinessException
import puni.extension.jackson.toJsonString
import puni.test.support.apiErrCodeMatches
import puni.test.support.errCodeMatches
import puni.test.support.httpStatusMatches

class TestSupportExtensionsTest : StringSpec({
  "should match errorCode" {
    errCodeMatches(ApiErrorCode.SERVER_ERROR) {
      throw BusinessException(ApiErrorCode.SERVER_ERROR)
    }
    shouldThrow<AssertionError> {
      errCodeMatches(ApiErrorCode.SERVER_ERROR) {}
    }
    shouldThrow<AssertionError> {
      errCodeMatches(ApiErrorCode.SERVER_ERROR) {
        throw BusinessException(ApiErrorCode.BAD_REQUEST)
      }
    }
  }
  "should match apiErrorCode" {
    apiErrCodeMatches(ApiErrorCode.SERVER_ERROR) {
      throw FeignException.FeignServerException(
        400,
        "bad",
        Request.create(Request.HttpMethod.GET, "/foo", emptyMap<String, MutableList<String>>(), ByteArray(0), Charsets.UTF_8),
        ApiErrorResponse(ApiErrorCode.SERVER_ERROR, emptyList()).toJsonString().toByteArray()
      )
    }
    shouldThrow<AssertionError> {
      apiErrCodeMatches(ApiErrorCode.SERVER_ERROR) {
        throw FeignException.FeignServerException(
          400,
          "bad",
          Request.create(Request.HttpMethod.GET, "/foo", emptyMap<String, MutableList<String>>(), ByteArray(0), Charsets.UTF_8),
          ApiErrorResponse(ApiErrorCode.BAD_REQUEST, emptyList()).toJsonString().toByteArray()
        )
      }
    }
    shouldThrow<AssertionError> {
      apiErrCodeMatches(ApiErrorCode.SERVER_ERROR) {}
    }
    shouldThrow<AssertionError> {
      apiErrCodeMatches(ApiErrorCode.SERVER_ERROR) {
        throw FeignException.FeignServerException(
          400,
          "bad",
          Request.create(Request.HttpMethod.GET, "/foo", emptyMap<String, MutableList<String>>(), ByteArray(0), Charsets.UTF_8),
          "".toByteArray()
        )
      }
    }
  }
  "should match httpStatus" {
    httpStatusMatches(HttpStatus.BAD_REQUEST) {
      throw FeignException.FeignServerException(
        400,
        "bad",
        Request.create(Request.HttpMethod.GET, "/foo", emptyMap<String, MutableList<String>>(), ByteArray(0), Charsets.UTF_8),
        ApiErrorResponse(ApiErrorCode.SERVER_ERROR, emptyList()).toJsonString().toByteArray()
      )
    }
    shouldThrow<AssertionError> {
      httpStatusMatches(HttpStatus.BAD_REQUEST) {
        throw FeignException.FeignServerException(
          401,
          "bad",
          Request.create(Request.HttpMethod.GET, "/foo", emptyMap<String, MutableList<String>>(), ByteArray(0), Charsets.UTF_8),
          ApiErrorResponse(ApiErrorCode.SERVER_ERROR, emptyList()).toJsonString().toByteArray()
        )
      }
    }
    shouldThrow<AssertionError> {
      httpStatusMatches(HttpStatus.BAD_REQUEST) {}
    }
    shouldThrow<AssertionError> {
      httpStatusMatches(HttpStatus.BAD_REQUEST) { throw RuntimeException("") }
    }
    shouldThrow<AssertionError> {
      httpStatusMatches(HttpStatus.BAD_REQUEST) {
        throw object : FeignException.FeignServerException(
          401,
          "bad",
          Request.create(Request.HttpMethod.GET, "/foo", emptyMap<String, MutableList<String>>(), ByteArray(0), Charsets.UTF_8),
          ApiErrorResponse(ApiErrorCode.SERVER_ERROR, emptyList()).toJsonString().toByteArray()
        ) {
          override fun contentUTF8(): String {
            throw RuntimeException("")
          }
        }
      }
    }
  }
})
