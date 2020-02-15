package puni.spring.authconfigure

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import puni.extension.jackson.JacksonCommon
import puni.spring.web.ApiExceptionHandler

@Configuration
class PuniSpringWebConfig {

  @Bean
  @ConditionalOnMissingBean
  fun apiExceptionHandler(@Autowired messageSource: MessageSource): ApiExceptionHandler = ApiExceptionHandler(messageSource)

  @Bean
  fun objectMapper(): ObjectMapper = JacksonCommon.objectMapper()
}
