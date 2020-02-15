package puni.spring.web.controller

import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import puni.data.dto.base.PagingRequest

@ApiModel
@ApiImplicitParams(
  ApiImplicitParam("s", name = "s", allowMultiple = true),
  ApiImplicitParam("foo", name = "foo", dataType = "ImplicitParamReq")
)
data class ImplicitParamReq(
  @ApiModelProperty
  val s: List<String>,
  val bar: String = "",
  val foo: NoImplicit = NoImplicit("bar")
)

@ApiModel
data class NoImplicit(
  val q: String
)

@RestController
@RequestMapping("/api")
class ApiImplicitParamTestController {

  @GetMapping("paging")
  fun getPage(req: PagingRequest): PagingRequest {
    return req
  }

  @PostMapping("paging")
  fun postPage(@RequestBody req: PagingRequest): PagingRequest {
    return req
  }

  @GetMapping("implicit")
  fun implicit(req: ImplicitParamReq): ImplicitParamReq {
    return req
  }

  @GetMapping("noImplicit")
  fun noImplicit(req: NoImplicit): NoImplicit {
    return req
  }
}
