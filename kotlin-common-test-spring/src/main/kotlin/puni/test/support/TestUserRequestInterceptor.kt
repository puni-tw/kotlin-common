package puni.test.support

import feign.RequestInterceptor
import feign.RequestTemplate

/**
 * @author leo
 */
class TestUserRequestInterceptor : RequestInterceptor {
  override fun apply(template: RequestTemplate) {
    template.header("Authorization", TestSupportContext.userToken)
  }
}
