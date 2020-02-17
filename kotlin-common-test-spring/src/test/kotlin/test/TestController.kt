package test

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

  @GetMapping("token")
  fun token(
    @RequestHeader("Authorization")
    token: String?
  ): String? {
    return token
  }
}
