package test

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import puni.test.support.SpringTestSupport

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = [TestApplication::class])
class SpringTestSupportTest : SpringTestSupport() {

  val testController by autowired<TestController>()

  @Test
  fun shouldAbleToGetAutowired() {
    testController shouldNotBe null
  }

  @Test
  fun shouldAbleCallWithoutToken() {
    api<TestApi>().token() shouldBe null
  }

  @Test
  fun shouldAbleCallWithToken() {
    val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
      ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ" +
      ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
    api<TestApi>(token).token() shouldBe token
  }
}
