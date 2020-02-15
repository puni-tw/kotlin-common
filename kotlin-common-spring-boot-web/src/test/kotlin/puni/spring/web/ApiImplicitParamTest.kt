package puni.spring.web

import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.boot.test.web.client.postForObject
import org.springframework.test.context.TestPropertySource
import puni.data.dto.base.PagingRequest
import puni.extension.jackson.jsonStringToMap
import puni.extension.jackson.toJsonString
import puni.spring.web.controller.ImplicitParamReq
import puni.spring.web.controller.NoImplicit
import puni.test.TestApplication

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [TestApplication::class])
@TestPropertySource(locations = ["classpath:api-test.properties"])
class ApiImplicitParamTest(
  val testRestTemplate: TestRestTemplate
) : StringSpec({
  "should able to get default paging" {
    testRestTemplate.getForObject<PagingRequest>(
      "/api/paging"
    ).also {
      it?.page shouldBe 1
      it?.pageSize shouldBe 10
    }
  }
  "should able to pass paging by get" {
    testRestTemplate.getForObject<PagingRequest>(
      "/api/paging?page={page}&pageSize={pageSize}",
      PagingRequest(page = 5, pageSize = 100).toJsonString().jsonStringToMap()
    ).also {
      it?.page shouldBe 5
      it?.pageSize shouldBe 100
    }
  }
  "should able to pass paging by post" {
    testRestTemplate.postForObject<PagingRequest>(
      "/api/paging",
      PagingRequest(page = 5, pageSize = 100)
    ).also {
      it?.page shouldBe 5
      it?.pageSize shouldBe 100
    }
  }
  "should able to pass multiple value by get" {
    testRestTemplate.getForObject<ImplicitParamReq>(
      "/api/implicit?s=a&s=b&s=c"
    ).also {
      it?.s.orEmpty().toList() shouldContain "a"
      it?.s.orEmpty().toList() shouldContain "b"
      it?.s.orEmpty().toList() shouldContain "c"
    }
  }
  "should able to pass noImplicit" {
    testRestTemplate.getForObject<NoImplicit>(
      "/api/noImplicit?q=foo"
    ).also {
      it?.q shouldBe "foo"
    }
  }
})
