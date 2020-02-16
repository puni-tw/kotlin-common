package puni.test.support

import org.junit.jupiter.api.BeforeAll
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext

/**
 * @author leo
 */
val AVAILABLE_PORTS = (10000..30000).toMutableList()

abstract class SpringTestSupport {

  companion object {
    @BeforeAll
    @JvmStatic
    fun setUp() {
      val port = AVAILABLE_PORTS.random().apply { AVAILABLE_PORTS.remove(this) }.toString()
      System.setProperty("server.port", port)
      LoggerFactory.getLogger(SpringTestSupport::class.java).info("server.port set to $port")
    }
  }

  @Autowired
  lateinit var applicationContext: ApplicationContext

  inline fun <reified T : Any> bean(): T = applicationContext.getBean<T>()

  inline fun <reified T : Any> autowired(): Lazy<T> = lazy { bean<T>() }
}
