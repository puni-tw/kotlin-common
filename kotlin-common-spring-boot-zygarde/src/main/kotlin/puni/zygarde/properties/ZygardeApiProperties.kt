package puni.zygarde.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("puni.zygarde.api")
class ZygardeApiProperties {
  var staticOptionApi = StaticOptionApiProp()

  class StaticOptionApiProp(
    var path: String = "/api/staticOptions"
  )
}
