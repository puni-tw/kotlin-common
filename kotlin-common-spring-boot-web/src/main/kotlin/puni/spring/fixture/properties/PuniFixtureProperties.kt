package puni.spring.fixture.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("puni.fixture")
class PuniFixtureProperties {
  var enabled: Boolean = true
}
