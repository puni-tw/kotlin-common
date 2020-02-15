package puni.spring.fixture.impl

import io.kotlintest.shouldBe
import org.springframework.stereotype.Component
import puni.spring.fixture.Fixture
import puni.spring.fixture.TestFixtureContext

@Component
class TestFixture1 : Fixture() {
  override fun order(): Int = 1
  override fun run() {
    TestFixtureContext.counter shouldBe 0
    TestFixtureContext.counter ++
  }
}
