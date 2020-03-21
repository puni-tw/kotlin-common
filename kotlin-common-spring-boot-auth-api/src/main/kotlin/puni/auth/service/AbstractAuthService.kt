package puni.auth.service

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.Date
import javax.crypto.spec.SecretKeySpec
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken
import puni.auth.AuthErrorCode
import puni.data.vo.auth.AuthenticatedVo
import puni.data.vo.auth.AuthenticationVo
import puni.data.vo.auth.UserLoginVo
import puni.exception.BusinessException
import puni.extension.jackson.jsonMapToObject

abstract class AbstractAuthService : UserDetailsService {

  val signingKeyBytes: ByteArray by lazy {
    getJwtKey().toByteArray()
  }
  val jwtSigningKey: SecretKeySpec by lazy {
    SecretKeySpec(
      signingKeyBytes,
      SignatureAlgorithm.HS512.jcaName
    )
  }

  abstract fun getJwtKey(): String

  abstract fun getJwtExpire(): Date

  abstract fun loadAuthenticationByUsername(username: String): AuthenticationVo

  abstract fun loadAuthenticationByUserLogin(userLoginVo: UserLoginVo): AuthenticatedVo

  abstract fun loadUserLoginByUsername(username: String): UserLoginVo

  override fun loadUserByUsername(username: String): UserDetails {
    val authentication = loadAuthenticationByUsername(username)
    return org.springframework.security.core.userdetails.User
      .withUsername(authentication.username)
      .password(authentication.password)
      .authorities(authentication.authorities)
      .accountExpired(false)
      .accountLocked(false)
      .credentialsExpired(false)
      .disabled(false)
      .build()
  }

  fun generateJwtToken(username: String): String {
    return Jwts.builder()
      .setIssuedAt(Date())
      .setExpiration(getJwtExpire())
      .addClaims(mapOf("userLogin" to loadUserLoginByUsername(username)))
      .signWith(jwtSigningKey)
      .compact()
  }

  fun validateTokenAndGetUserLogin(token: String): Authentication {
    try {
      val userLogin = Jwts.parserBuilder()
        .setSigningKey(signingKeyBytes)
        .build()
        .parseClaimsJws(token)
        .body
        .get("userLogin", Map::class.java)
        .jsonMapToObject<UserLoginVo>()
      val authentication = loadAuthenticationByUserLogin(userLogin)
      return PreAuthenticatedAuthenticationToken(
        authentication.username,
        null,
        authentication.authorities
      ).also {
        it.details = userLogin
      }
    } catch (e: ExpiredJwtException) {
      throw BusinessException(AuthErrorCode.TOKEN_EXPIRED)
    }
  }
}
