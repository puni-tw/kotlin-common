package puni.spring.web

import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import org.springframework.web.filter.GenericFilterBean

class TestErrorFilter : GenericFilterBean() {
  override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
    if ((req as HttpServletRequest).requestURI.startsWith("/filterError")) {
      throw IllegalStateException("error in filter")
    } else {
      chain.doFilter(req, res)
    }
  }
}
