package puni.spring.fixture.impl

import org.springframework.stereotype.Component
import puni.spring.fixture.Fixture
import puni.spring.fixture.TestFixtureContext

@Component
class TestFixture : Fixture() {
  override fun run() {
    TestFixtureContext.counter = 0
  }
}
