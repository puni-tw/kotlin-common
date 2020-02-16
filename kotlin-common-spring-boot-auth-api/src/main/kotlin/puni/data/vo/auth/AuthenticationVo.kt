package puni.data.vo.auth

import org.springframework.security.core.GrantedAuthority

data class AuthenticationVo(
  val username: String,
  val password: String,
  val authorities: Collection<GrantedAuthority>
)
