package puni.data.dto.auth

import io.swagger.annotations.ApiModel

@ApiModel
data class AuthResponse(
  val token: String
)
