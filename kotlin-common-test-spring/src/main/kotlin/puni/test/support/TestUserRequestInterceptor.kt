package puni.test.support

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.stereotype.Component

/**
 * @author leo
 */
@Component
class TestUserRequestInterceptor : RequestInterceptor {
  override fun apply(template: RequestTemplate) {
    template.header("Authorization", TestSupportContext.userToken)
  }
}
