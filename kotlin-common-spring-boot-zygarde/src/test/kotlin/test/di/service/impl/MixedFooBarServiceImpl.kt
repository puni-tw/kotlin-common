package test.di.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import puni.zygarde.di.autowired
import puni.zygarde.di.bean
import test.di.service.BarProviderService
import test.di.service.FooProviderService
import test.di.service.HelloService
import test.di.service.MixedFooBarService

@Service
class MixedFooBarServiceImpl(
  @Autowired val helloService: HelloService
) : MixedFooBarService {
  val fooService by autowired<FooProviderService>()
  override fun foobar(): String {
    return bean<BarProviderService>().bar() +
      fooService.foo() +
      helloService.hello()
  }
}
