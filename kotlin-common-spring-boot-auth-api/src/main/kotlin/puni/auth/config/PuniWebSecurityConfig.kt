package puni.auth.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import puni.auth.filter.AuthFilter

/**
 * @author leo
 */
abstract class PuniWebSecurityConfig : WebSecurityConfigurerAdapter() {

  @Autowired
  protected lateinit var authFilter: AuthFilter

  @Bean
  override fun authenticationManager(): AuthenticationManager {
    return super.authenticationManager()
  }

  override fun configure(http: HttpSecurity) {
    http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter::class.java)

    http
      .httpBasic().disable()
      .csrf().disable()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()

      .authorizeRequests()
      .antMatchers("/api/**").authenticated()

      .anyRequest()
      .permitAll()
  }

  @ConditionalOnMissingBean
  @Bean
  open fun passwordEncode(): PasswordEncoder = BCryptPasswordEncoder()
}
