package puni.spring.web

import javax.validation.Valid
import javax.validation.constraints.Email
import org.hibernate.validator.constraints.Length
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import puni.exception.ApiErrorCode
import puni.exception.BusinessException
import puni.extension.exception.errWhen

/**
 * @author leo
 */
@RestController
class ApiExceptionHandlerTestController {

  data class FooRequest(
    @field:Length(min = 2, max = 10)
    val foo: String,
    @field:Email val email: String = ""
  )

  @PostMapping("foo")
  fun foo(
    @Valid @RequestBody
    req: FooRequest
  ): FooRequest = req

  @GetMapping("bar")
  fun bar() {
    throw IllegalStateException("dont call me")
  }

  @GetMapping("bs1")
  fun bs1() {
    "foo".errWhen(ApiErrorCode.SERVER_ERROR) { it == "foo" }
  }

  @GetMapping("bs2")
  fun bs2() {
    throw BusinessException(ApiErrorCode.SERVER_ERROR, RuntimeException("custom exception message"))
  }

  @GetMapping("bs3")
  fun bs3() {
    throw BusinessException(ApiErrorCode.SERVER_ERROR, "{} {} {}", "I", "am", "Groot")
  }

  @GetMapping("re1")
  fun runtimeExceptionWithBusinessException() {
    throw RuntimeException("foo", BusinessException(ApiErrorCode.SERVER_ERROR))
  }

  @GetMapping("re2")
  fun runtimeExceptionEmpty() {
    throw RuntimeException()
  }
}
