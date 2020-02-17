package test

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "TestApi")
interface TestApi {

  @GetMapping("token")
  fun token(): String
}
