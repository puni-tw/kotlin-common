package puni.spring.authconfigure

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import puni.spring.fixture.FixtureRunner

@Configuration
class PuniSpringFixtureConfig {

  @Bean
  @ConditionalOnMissingBean
  fun fixtureRunner(applicationContext: ApplicationContext): FixtureRunner = FixtureRunner(applicationContext)
}
