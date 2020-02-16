package puni.spring.autoconfigure

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import puni.extension.jackson.JacksonCommon
import puni.spring.web.exception.ApiExceptionFilter
import puni.spring.web.exception.ApiExceptionHandler
import puni.spring.web.exception.mapper.MissingKotlinParameterExceptionMapper

@Configuration
class PuniSpringWebConfig {

  @Bean
  fun missingKotlinParameterExceptionMapper(): MissingKotlinParameterExceptionMapper = MissingKotlinParameterExceptionMapper()

  @Bean
  @ConditionalOnMissingBean
  fun apiExceptionHandler(): ApiExceptionHandler = ApiExceptionHandler()

  @Bean
  fun objectMapper(): ObjectMapper = JacksonCommon.objectMapper()

  @Bean
  @ConditionalOnMissingBean
  fun apiExceptionFilter(
    apiExceptionHandler: ApiExceptionHandler,
    objectMapper: ObjectMapper
  ): ApiExceptionFilter = ApiExceptionFilter(apiExceptionHandler, objectMapper)
}
