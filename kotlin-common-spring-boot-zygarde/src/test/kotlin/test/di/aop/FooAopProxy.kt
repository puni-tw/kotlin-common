package test.di.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import puni.zygarde.di.DiService
import puni.zygarde.di.bean
import test.di.service.BarProviderService

@Component
@Aspect
class FooAopProxy : DiService {

  @Around("@annotation(test.di.aop.FooAop)")
  @Throws(Throwable::class)
  fun foo(joinPoint: ProceedingJoinPoint): Any? {
    return bean<BarProviderService>().bar()
  }
}
