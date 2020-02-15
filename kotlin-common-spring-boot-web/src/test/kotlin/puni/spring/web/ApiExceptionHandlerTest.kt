package puni.spring.web

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.boot.test.web.client.postForObject
import org.springframework.test.context.TestPropertySource
import puni.data.dto.ApiErrorResponse
import puni.exception.ApiErrorCode.BAD_REQUEST
import puni.exception.ApiErrorCode.SERVER_ERROR
import puni.test.TestApplication

/**
 * @author leo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [TestApplication::class])
@TestPropertySource(locations = ["classpath:api-test.properties"])
class ApiExceptionHandlerTest(
  val testRestTemplate: TestRestTemplate
) : StringSpec({
  "should be BAD_REQUEST when missing parameter" {
    testRestTemplate.postForObject<ApiErrorResponse>("/foo", emptyMap<String, String>()).apply {
      this?.messages?.first() shouldBe "missing parameter 'foo'"
      this?.code shouldBe BAD_REQUEST.code
      this?.name shouldBe BAD_REQUEST.name
    }
  }
  "should be BAD_REQUEST when argument not valid" {
    testRestTemplate.postForObject<ApiErrorResponse>("/foo", ApiExceptionHandlerTestController.FooRequest("a")).apply {
      this?.messages?.first() shouldBe "foo length must be between 2 and 10"
      this?.code shouldBe BAD_REQUEST.code
      this?.name shouldBe BAD_REQUEST.name
    }
  }
  "should be BAD_REQUEST and get messages defined in messages.properties" {
    testRestTemplate.postForObject<ApiErrorResponse>("/foo", ApiExceptionHandlerTestController.FooRequest("aaa", "abc")).apply {
      this?.messages?.first() shouldBe "Email格式不正確"
      this?.code shouldBe BAD_REQUEST.code
      this?.name shouldBe BAD_REQUEST.name
    }
  }
  "should be SERVER_ERROR can get correct exception message" {
    testRestTemplate.getForObject<ApiErrorResponse>("/bar").apply {
      this?.messages?.first() shouldBe "dont call me"
      this?.code shouldBe SERVER_ERROR.code
      this?.name shouldBe SERVER_ERROR.name
    }
  }
  "should be SERVER_ERROR can get default server error message" {
    testRestTemplate.getForObject<ApiErrorResponse>("/bs1").apply {
      this?.messages?.first() shouldBe SERVER_ERROR.message
      this?.code shouldBe SERVER_ERROR.code
      this?.name shouldBe SERVER_ERROR.name
    }
  }
  "should be SERVER_ERROR can get custom message" {
    testRestTemplate.getForObject<ApiErrorResponse>("/bs2").apply {
      this?.messages?.first() shouldBe SERVER_ERROR.message
      this?.messages?.get(1) shouldBe "custom exception message"
      this?.code shouldBe SERVER_ERROR.code
      this?.name shouldBe SERVER_ERROR.name
    }
  }
  "should be SERVER_ERROR can get placed message" {
    testRestTemplate.getForObject<ApiErrorResponse>("/bs3").apply {
      this?.messages?.first() shouldBe "I am Groot"
      this?.code shouldBe SERVER_ERROR.code
      this?.name shouldBe SERVER_ERROR.name
    }
  }
  "should be SERVER_ERROR when throwing RuntimeException with cause BusinessException" {
    testRestTemplate.getForObject<ApiErrorResponse>("/re1").apply {
      this?.messages?.first() shouldBe SERVER_ERROR.message
      this?.code shouldBe SERVER_ERROR.code
      this?.name shouldBe SERVER_ERROR.name
    }
  }
  "should be SERVER_ERROR when empty RuntimeException" {
    testRestTemplate.getForObject<ApiErrorResponse>("/re2").apply {
      this?.messages?.first() shouldBe "RuntimeException"
      this?.code shouldBe SERVER_ERROR.code
      this?.name shouldBe SERVER_ERROR.name
    }
  }
})
