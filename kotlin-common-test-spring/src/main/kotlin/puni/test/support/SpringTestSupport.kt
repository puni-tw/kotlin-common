package puni.test.support

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import org.junit.jupiter.api.BeforeAll
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.getBean
import org.springframework.cglib.proxy.InvocationHandler
import org.springframework.cglib.proxy.Proxy
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
    private set

  inline fun <reified T : Any> bean(): T = applicationContext.getBean<T>()

  inline fun <reified T : Any> autowired(): Lazy<T> = lazy { bean<T>() }

  inline fun <reified T : Any> api(token: String? = null): T {
    val bean = bean<T>()
    return Proxy.newProxyInstance(
      T::class.java.classLoader,
      arrayOf(T::class.java),
      ApiProxyInvocationHandler(bean, token)
    ) as T
  }

  class ApiProxyInvocationHandler<T>(
    private val bean: T,
    private val token: String?
  ) : InvocationHandler {
    override fun invoke(proxy: Any?, method: Method, args: Array<out Any>?): Any? {
      try {
        TestSupportContext.userToken = token
        return method.invoke(bean, *(args ?: emptyArray()))
      } catch (t: InvocationTargetException) {
        throw t.cause ?: t
      } finally {
        TestSupportContext.userToken = null
      }
    }
  }
}
