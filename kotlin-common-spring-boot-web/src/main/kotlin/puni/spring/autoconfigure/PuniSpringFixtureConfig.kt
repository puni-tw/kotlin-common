package puni.spring.autoconfigure

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import puni.spring.fixture.FixtureRunner

@Configuration
class PuniSpringFixtureConfig {

  @Bean
  @ConditionalOnProperty("puni.fixture.enabled", havingValue = "true")
  @ConditionalOnMissingBean
  fun fixtureRunner(applicationContext: ApplicationContext): FixtureRunner = FixtureRunner(applicationContext)
}
