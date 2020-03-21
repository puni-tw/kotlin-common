package puni.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("puni.auth")
class PuniAuthProperties {
  var securityConfig: SecurityConfig = SecurityConfig()

  class SecurityConfig {
    var enabled: Boolean = false
  }
}
