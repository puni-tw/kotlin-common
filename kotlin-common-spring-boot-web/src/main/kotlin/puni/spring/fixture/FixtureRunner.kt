package puni.spring.fixture

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import puni.log.Loggable

@Order(Ordered.LOWEST_PRECEDENCE)
class FixtureRunner(
  private val applicationContext: ApplicationContext
) : Loggable, ApplicationListener<ContextRefreshedEvent> {

  override fun onApplicationEvent(event: ContextRefreshedEvent) {
    runAllFixtures()
  }

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
