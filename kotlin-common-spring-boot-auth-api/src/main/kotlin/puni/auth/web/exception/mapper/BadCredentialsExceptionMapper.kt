package puni.auth.web.exception.mapper

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Component
import puni.exception.ApiErrorCode
import puni.exception.BusinessException
import puni.spring.web.exception.ExceptionToBusinessExceptionMapper

@Component
class BadCredentialsExceptionMapper : ExceptionToBusinessExceptionMapper<BadCredentialsException>() {
  override fun supported(t: Throwable): Boolean = t is BadCredentialsException

  override fun transform(t: BadCredentialsException): BusinessException {
    return BusinessException(ApiErrorCode.UNAUTHORIZED)
  }
}
