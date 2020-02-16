package puni.auth.filter

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import puni.auth.service.AbstractAuthService
import puni.log.Loggable

/**
 * @author leo
 */
@Component
class AuthFilter(
  @Autowired val authService: AbstractAuthService
) : OncePerRequestFilter(), Loggable {

  override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
    if (req.requestURI.startsWith("/api")) {
      val authorizationHeader = req.getHeader("Authorization") ?: req.getHeader("authorization")
      if (authorizationHeader != null) {
        SecurityContextHolder.getContext().authentication = authService.validateTokenAndGetUserLogin(authorizationHeader)
      } else {
        SecurityContextHolder.clearContext()
      }
    }
    chain.doFilter(req, res)
  }
}
