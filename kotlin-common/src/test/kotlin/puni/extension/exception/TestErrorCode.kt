package puni.extension.exception

import puni.exception.ErrorCode

enum class TestErrorCode(
  override val code: String,
  override val message: String
) : ErrorCode {
  ERROR("999", "error")
}
