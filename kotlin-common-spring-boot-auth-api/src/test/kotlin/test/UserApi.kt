package test

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import puni.auth.api.SecuredApi
import puni.data.vo.auth.UserLoginVo

@RestController
@RequestMapping("/api/user")
class UserApi : SecuredApi {

  @GetMapping
  fun me(): UserLoginVo {
    return currentUserLogin
  }
}
