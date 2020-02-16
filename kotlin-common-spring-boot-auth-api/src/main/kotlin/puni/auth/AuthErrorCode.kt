package puni.auth

import puni.exception.ErrorCode

enum class AuthErrorCode(
  override val code: String,
  override val message: String
) : ErrorCode {
  TOKEN_EXPIRED("403", "Token expired")
}
