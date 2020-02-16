package puni.auth.web.exception.mapper

import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import puni.exception.ApiErrorCode
import puni.exception.BusinessException
import puni.spring.web.exception.ExceptionToBusinessExceptionMapper

@Component
class AccessDeniedExceptionMapper : ExceptionToBusinessExceptionMapper<AccessDeniedException>() {
  override fun supported(t: Throwable): Boolean = t is AccessDeniedException

  override fun transform(t: AccessDeniedException): BusinessException {
    return BusinessException(ApiErrorCode.UNAUTHORIZED)
  }
}
