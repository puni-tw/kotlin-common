package puni.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import puni.spring.web.ApiExceptionHandlerTestController

/**
 * @author leo
 */
@SpringBootApplication
class TestApplication {

  @Bean
  fun apiExceptionHandlerTestController() = ApiExceptionHandlerTestController()
}
