package puni.data.dto

import io.swagger.annotations.ApiModel
import puni.exception.ErrorCode

/**
 * @author leo
 */
@ApiModel
class ApiErrorResponse(
  val code: String,
  val name: String,
  val messages: List<String>
) {
  constructor(errorCode: ErrorCode, messages: List<String>) : this(
    errorCode.code,
    errorCode.name,
    messages
  )
}
