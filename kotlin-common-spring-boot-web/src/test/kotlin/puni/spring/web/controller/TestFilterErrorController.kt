package puni.spring.web.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import puni.data.dto.base.PagingRequest

@RestController
@RequestMapping("/filterError")
class TestFilterErrorController {

  @GetMapping
  fun getPage(req: PagingRequest) = "ok"
}
