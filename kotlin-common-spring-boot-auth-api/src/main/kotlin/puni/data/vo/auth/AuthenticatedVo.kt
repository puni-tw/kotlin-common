package puni.data.vo.auth

import org.springframework.security.core.GrantedAuthority

data class AuthenticatedVo(
  val username: String,
  val authorities: Collection<GrantedAuthority>
)
