package puni.test.spring.config

import feign.Client
import feign.Feign
import feign.Logger
import feign.Request
import feign.Target
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.AnnotationUtils.findAnnotation
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import puni.log.Loggable

@Configuration
class PuniSpringTestFeignConfig(
  @Value("\${server.port}")
  val serverPort: Int
) : Loggable {

  @Bean
  fun feignBuilder(): Feign.Builder {
    return object : Feign.Builder() {
      override fun <T : Any?> target(target: Target<T>): T {
        LOGGER.info("creating feign client of ${target.type().canonicalName}")
        return build().newInstance(Target.HardCodedTarget(target.type(), "http://localhost:$serverPort"))
      }
    }
  }

  @Bean
  fun feignClient(): Client {
    return Client.Default(null, null)
  }

  @Bean
  fun feignLoggerLevel(): Logger.Level {
    return Logger.Level.FULL
  }

  @Bean
  fun options(): Request.Options {
    return Request.Options(30000, 30000)
  }

  @Bean
  fun feignWebRegistrations(): WebMvcRegistrations {
    return object : WebMvcRegistrations {
      override fun getRequestMappingHandlerMapping(): RequestMappingHandlerMapping {
        return FeignFilterRequestMappingHandlerMapping()
      }
    }
  }

  private class FeignFilterRequestMappingHandlerMapping : RequestMappingHandlerMapping() {
    override fun isHandler(beanType: Class<*>): Boolean {
      return (
        super.isHandler(beanType) && (
          findAnnotation(beanType, RestController::class.java) != null ||
            findAnnotation(beanType, Controller::class.java) != null
          )
        )
    }
  }
}
