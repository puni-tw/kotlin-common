package puni.auth.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import puni.data.dto.auth.AuthRequest
import puni.data.dto.auth.AuthResponse

@FeignClient(name = "AuthApi")
@Api(tags = ["Auth"])
@RequestMapping("/auth")
interface AuthApi {

  @ApiOperation("Auth")
  @PostMapping
  fun auth(@RequestBody req: AuthRequest): AuthResponse
}
