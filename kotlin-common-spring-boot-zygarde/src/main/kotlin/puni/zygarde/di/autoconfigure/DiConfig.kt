package puni.zygarde.di.autoconfigure

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import puni.zygarde.di.DiServiceContext

@Configuration
class DiConfig {
  @Bean
  fun diServiceContext() = DiServiceContext
}
