package puni.spring.web

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import puni.data.dto.ApiErrorResponse
import puni.exception.ApiErrorCode
import puni.exception.BusinessException
import puni.log.Loggable

/**
 * @author leo
 */
@ControllerAdvice
open class ApiExceptionHandler(
  @Autowired val messageSource: MessageSource
) : Loggable {

  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handleValidationError(e: MethodArgumentNotValidException): ResponseEntity<ApiErrorResponse> {
    val locale = LocaleContextHolder.getLocale()
    val bindingResult = e.bindingResult
    val errorMessages = bindingResult.fieldErrors
      .map { objectError ->
        objectError.codes?.forEach {
          try {
            return@map messageSource.getMessage(it, objectError.arguments, locale)
          } catch (ex: NoSuchMessageException) {
            LOGGER.debug(ex.message)
          }
        }
        "${objectError.field} ${objectError.defaultMessage}"
      }

    return ResponseEntity(
      ApiErrorResponse(
        errorCode = ApiErrorCode.BAD_REQUEST,
        messages = errorMessages
      ),
      HttpStatus.BAD_REQUEST
    )
  }

  @ExceptionHandler(BusinessException::class)
  fun handleBusinessException(e: BusinessException): ResponseEntity<ApiErrorResponse> {
    LOGGER.error(e.message, e)
    val res = ApiErrorResponse(
      errorCode = e.errorCode,
      messages = listOfNotNull(e.message, e.cause?.message)
    )
    return ResponseEntity(res, HttpStatus.EXPECTATION_FAILED)
  }

  @ExceptionHandler(MissingKotlinParameterException::class)
  fun handleMissingKotlinParameterException(e: MissingKotlinParameterException): ResponseEntity<ApiErrorResponse> {
    return ResponseEntity(
      ApiErrorResponse(
        errorCode = ApiErrorCode.BAD_REQUEST,
        messages = listOf("missing parameter '${e.parameter.name}'")
      ),
      HttpStatus.BAD_REQUEST
    )
  }

  @ExceptionHandler(Throwable::class)
  fun handleThrowable(t: Throwable): ResponseEntity<ApiErrorResponse> {
    return t.cause
      ?.let { handleThrowableInternal(it, this::handleThrowable) }
      ?: handleThrowableInternal(t) {
        LOGGER.error(t.message, t)
        ResponseEntity(
          ApiErrorResponse(
            errorCode = ApiErrorCode.SERVER_ERROR,
            messages = listOf(t.message ?: t.javaClass.simpleName)
          ),
          HttpStatus.INTERNAL_SERVER_ERROR
        )
      }
  }

  protected fun handleThrowableInternal(
    t: Throwable,
    onNoMatch: (t: Throwable) -> ResponseEntity<ApiErrorResponse>
  ) = when (t) {
    is BusinessException -> handleBusinessException(t)
    is MissingKotlinParameterException -> handleMissingKotlinParameterException(t)
    else -> onNoMatch(t)
  }
}
