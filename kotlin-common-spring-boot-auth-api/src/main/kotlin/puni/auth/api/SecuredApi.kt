package puni.auth.api

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import puni.data.vo.auth.UserLoginVo

/**
 * @author leo
 */
interface SecuredApi {
  val currentUserLogin: UserLoginVo
    get() = SecurityContextHolder.getContext().authentication.let {
      if (it.isAuthenticated && it.details is UserLoginVo) {
        return it.details as UserLoginVo
      }
      throw AccessDeniedException("not logged in")
    }
}
