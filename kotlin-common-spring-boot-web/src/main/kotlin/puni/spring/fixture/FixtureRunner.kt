package puni.spring.fixture

import javax.annotation.PostConstruct
import org.springframework.context.ApplicationContext
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import puni.log.Loggable

@Order(Ordered.LOWEST_PRECEDENCE)
class FixtureRunner(
  private val applicationContext: ApplicationContext
) : Loggable {

  @PostConstruct
  fun runAllFixtures() {
    applicationContext.getBeansOfType(Fixture::class.java)
      .map { it.value }
      .also {
        LOGGER.info("${it.size} fixtures detected")
      }
      .sortedBy { it.order() }
      .forEach {
        it.run()
        LOGGER.info("fixture ${it.javaClass.canonicalName} completed")
      }
  }
}
