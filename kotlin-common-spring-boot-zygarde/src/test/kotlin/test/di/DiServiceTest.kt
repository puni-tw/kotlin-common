package test.di

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import puni.test.support.SpringTestSupport
import test.ZygardeTestApplication
import test.di.service.BarProviderService
import test.di.service.FooProviderService
import test.di.service.HelloService
import test.di.service.MixedFooBarService

@ActiveProfiles("di")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [ZygardeTestApplication::class])
class DiServiceTest : SpringTestSupport() {

  @Test
  fun `should able to use services implement DiService`() {
    bean<FooProviderService>().foo() shouldBe "bar"
    bean<BarProviderService>().bar() shouldBe "bar"
    bean<HelloService>().hello() shouldBe "barbar"
    bean<MixedFooBarService>().foobar() shouldBe "barbarbarbar"
  }
}
