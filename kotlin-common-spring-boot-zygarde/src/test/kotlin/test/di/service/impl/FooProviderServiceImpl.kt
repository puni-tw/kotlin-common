package test.di.service.impl

import org.springframework.stereotype.Service
import puni.zygarde.di.bean
import test.di.service.BarProviderService
import test.di.service.FooProviderService

@Service
class FooProviderServiceImpl : FooProviderService {
  override fun foo(): String {
    return bean<BarProviderService>().bar()
  }
}
