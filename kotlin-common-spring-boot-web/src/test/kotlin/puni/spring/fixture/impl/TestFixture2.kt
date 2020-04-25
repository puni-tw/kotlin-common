package puni.spring.fixture.impl

import io.kotlintest.shouldBe
import org.springframework.stereotype.Component
import puni.spring.fixture.Fixture
import puni.spring.fixture.TestFixtureContext

@Component
class TestFixture2 : Fixture() {
  override fun order(): Int = 2
  override fun run() {
    TestFixtureContext.counter shouldBe 1
    TestFixtureContext.counter += 1
  }
}
