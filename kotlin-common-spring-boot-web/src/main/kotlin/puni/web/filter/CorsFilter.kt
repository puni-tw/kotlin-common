package puni.web.filter

import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean

/**
 * @author leo
 */
@Component
@Order(Int.MIN_VALUE)
class CorsFilter : GenericFilterBean() {
  override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
    val req = request as HttpServletRequest
    val res = response as HttpServletResponse
    res.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"))
    res.setHeader("Access-Control-Allow-Credentials", "true")
    res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE")
    res.setHeader("Access-Control-Max-Age", "3600")
    res.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization")
    res.setHeader("Access-Control-Expose-Headers", "Authorization")
    if (req.method == "OPTIONS") {
      res.status = 200
    } else {
      chain.doFilter(request, response)
    }
  }
}
