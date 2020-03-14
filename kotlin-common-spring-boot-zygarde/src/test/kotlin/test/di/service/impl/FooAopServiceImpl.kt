package test.di.service.impl

import org.springframework.stereotype.Service
import puni.zygarde.di.bean
import test.di.aop.FooAop
import test.di.service.BarProviderService
import test.di.service.FooAopService

@Service
class FooAopServiceImpl : FooAopService {
  @FooAop
  override fun foo(): String {
    return "foo"
  }

  override fun bar(): String {
    return bean<BarProviderService>().bar()
  }
}
