package test

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import puni.auth.service.AbstractAuthService
import puni.data.vo.auth.AuthenticatedVo
import puni.data.vo.auth.AuthenticationVo
import puni.data.vo.auth.UserLoginVo

@Service
class AuthServiceImpl : AbstractAuthService() {

  override fun getJwtKey(): String = "1d2b66af-8a83-4965-b271-84d5330e0e0c"

  override fun getJwtExpire(): Date = Date.from(
    LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant()
  )

  override fun loadUserLoginByUsername(username: String): UserLoginVo {
    return UserLoginVo(id = 0)
  }

  override fun loadAuthenticationByUsername(username: String): AuthenticationVo {
    return AuthenticationVo(username = username, password = BCryptPasswordEncoder().encode("bar"), authorities = emptyList())
  }

  override fun loadAuthenticationByUserLogin(userLoginVo: UserLoginVo): AuthenticatedVo {
    return AuthenticatedVo(username = "foo", authorities = emptyList())
  }
}
