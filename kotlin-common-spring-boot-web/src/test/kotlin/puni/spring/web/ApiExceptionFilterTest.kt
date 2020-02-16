package puni.spring.web

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.test.context.TestPropertySource
import puni.data.dto.ApiErrorResponse
import puni.exception.ApiErrorCode
import puni.test.TestApplication

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [TestApplication::class])
@TestPropertySource(locations = ["classpath:api-test.properties"])
class ApiExceptionFilterTest(
  val testRestTemplate: TestRestTemplate
) : StringSpec({
  "should be BAD_REQUEST when missing parameter" {
    testRestTemplate.getForObject<ApiErrorResponse>("/filterError").apply {
      this?.messages?.first() shouldBe "error in filter"
      this?.code shouldBe ApiErrorCode.SERVER_ERROR.code
    }
  }
})
