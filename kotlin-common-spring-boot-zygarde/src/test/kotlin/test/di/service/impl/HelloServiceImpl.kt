package test.di.service.impl

import org.springframework.stereotype.Service
import puni.zygarde.di.DiService
import puni.zygarde.di.autowired
import test.di.service.BarProviderService
import test.di.service.FooProviderService
import test.di.service.HelloService

@Service
class HelloServiceImpl : HelloService, DiService {
  val fooService by autowired<FooProviderService>()
  val barService by autowired<BarProviderService>()
  override fun hello(): String {
    return fooService.foo() + barService.bar()
  }
}
