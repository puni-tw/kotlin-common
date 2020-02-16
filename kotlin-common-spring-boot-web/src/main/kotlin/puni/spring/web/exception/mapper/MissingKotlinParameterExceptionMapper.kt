package puni.spring.web.exception.mapper

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import puni.exception.ApiErrorCode
import puni.exception.BusinessException
import puni.spring.web.exception.ExceptionToBusinessExceptionMapper

class MissingKotlinParameterExceptionMapper : ExceptionToBusinessExceptionMapper<MissingKotlinParameterException>() {
  override fun supported(t: Throwable): Boolean = t is MissingKotlinParameterException

  override fun transform(t: MissingKotlinParameterException): BusinessException {
    return BusinessException(ApiErrorCode.BAD_REQUEST, "missing parameter '${t.parameter.name}'")
  }
}
