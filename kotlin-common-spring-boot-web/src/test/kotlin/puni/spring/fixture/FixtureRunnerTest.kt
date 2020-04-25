package puni.spring.fixture

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.springframework.boot.test.context.SpringBootTest
import puni.test.TestApplication

/**
 * @author leo
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [TestApplication::class])
class FixtureRunnerTest : StringSpec({
  "should have correct value after fixture ran" {
    TestFixtureContext.counter shouldBe 2
  }
})
