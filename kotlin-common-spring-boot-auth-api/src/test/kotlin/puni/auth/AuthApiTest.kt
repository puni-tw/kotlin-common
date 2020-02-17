package puni.auth

import com.ninjasquad.springmockk.SpykBean
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.mockk.every
import java.net.URI
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.test.context.ActiveProfiles
import puni.auth.api.AuthApi
import puni.data.dto.auth.AuthRequest
import puni.data.vo.auth.UserLoginVo
import puni.test.support.SpringTestSupport
import puni.test.support.httpStatusMatches
import test.AuthServiceImpl
import test.PuniSpringAuthApiTestApplication

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = [PuniSpringAuthApiTestApplication::class])
class AuthApiTest : SpringTestSupport() {

  @SpykBean
  private lateinit var authServiceImpl: AuthServiceImpl

  @Test
  fun `should able to login`() {
    val token = api<AuthApi>().auth(AuthRequest("foo", "bar")).token
    token shouldNotBe null

    bean<TestRestTemplate>().exchange<UserLoginVo>(
      RequestEntity.get(URI("/api/user"))
        .header("Authorization", token)
        .build()
    ).also {
      it.body shouldNotBe null
      it.body?.id shouldNotBe null
    }
  }

  @Test
  fun `should not able to call api without token`() {
    bean<TestRestTemplate>().exchange<UserLoginVo>(
      RequestEntity.get(URI("/api/user"))
        .build()
    ).also {
      it.statusCode shouldBe HttpStatus.UNAUTHORIZED
    }
  }

  @Test
  fun `should not able to call api when token expired`() {
    every { authServiceImpl.getJwtExpire() } returns Date.from(LocalDateTime.now().minusYears(1).atZone(ZoneId.systemDefault()).toInstant())
    val token = bean<AuthApi>().auth(AuthRequest("foo", "bar")).token
    token shouldNotBe null

    bean<TestRestTemplate>().exchange<UserLoginVo>(
      RequestEntity.get(URI("/api/user"))
        .header("Authorization", token)
        .build()
    ).also {
      it.statusCode shouldBe HttpStatus.EXPECTATION_FAILED
    }
  }

  @Test
  fun `should not able to login`() = httpStatusMatches(HttpStatus.UNAUTHORIZED) {
    val authApi = bean<AuthApi>()
    authApi.auth(AuthRequest("foo", "bar2"))
  }

  @Test
  fun `should not able to get currentUser when not authenticated`() {
    bean<TestRestTemplate>().exchange<UserLoginVo>(
      RequestEntity.get(URI("/user")).build()
    ).also {
      it.statusCode shouldBe HttpStatus.UNAUTHORIZED
    }
  }
}
