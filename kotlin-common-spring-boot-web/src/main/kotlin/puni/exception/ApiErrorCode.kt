package puni.exception

enum class ApiErrorCode(
  override val code: String,
  override val message: String
) : ErrorCode {
  BAD_REQUEST("400", "Bad request"),
  UNAUTHORIZED("401", "Unauthorized"),
  SERVER_ERROR("500", "Server error")
}
