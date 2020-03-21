package test

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import puni.auth.config.PuniWebSecurityConfig

@Configuration
@EnableWebSecurity
class TestSecurityConfig : PuniWebSecurityConfig()
