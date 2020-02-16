package puni.auth.api.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RestController
import puni.auth.api.AuthApi
import puni.auth.service.AbstractAuthService
import puni.data.dto.auth.AuthRequest
import puni.data.dto.auth.AuthResponse

@RestController
class AuthController(
  @Autowired val authenticationManager: AuthenticationManager,
  @Autowired val abstractAuthService: AbstractAuthService
) : AuthApi {

  override fun auth(req: AuthRequest): AuthResponse {
    val authentication = authenticationManager.authenticate(
      UsernamePasswordAuthenticationToken(req.account, req.password)
    )
    SecurityContextHolder.getContext().authentication = authentication

    val token = abstractAuthService.generateJwtToken(authentication.name)
    return AuthResponse(token)
  }
}
