package puni.exception

import org.springframework.http.HttpStatus

enum class ApiErrorCode(
  override val code: String,
  override val message: String,
  val httpStatus: HttpStatus
) : ErrorCode {
  BAD_REQUEST("400", "Bad request", HttpStatus.BAD_REQUEST),
  UNAUTHORIZED("401", "Unauthorized", HttpStatus.UNAUTHORIZED),
  SERVER_ERROR("500", "Server error", HttpStatus.INTERNAL_SERVER_ERROR)
}
