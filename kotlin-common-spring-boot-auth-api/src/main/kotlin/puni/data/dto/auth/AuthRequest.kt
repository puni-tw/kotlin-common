package puni.data.dto.auth

import io.swagger.annotations.ApiModel

@ApiModel
data class AuthRequest(
  val account: String,
  val password: String
)
