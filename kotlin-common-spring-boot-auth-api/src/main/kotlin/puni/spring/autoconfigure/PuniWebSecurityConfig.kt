package puni.spring.autoconfigure

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import puni.auth.filter.AuthFilter

/**
 * @author leo
 */
@Order(1000)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
@Configuration
@ComponentScan("puni.auth")
@ConditionalOnProperty("puni.auth.security-config.enabled", havingValue = "true")
class PuniWebSecurityConfig : WebSecurityConfigurerAdapter() {

  @Autowired
  private lateinit var authFilter: AuthFilter

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

  @Bean
  fun passwordEncode(): PasswordEncoder = BCryptPasswordEncoder()
}
