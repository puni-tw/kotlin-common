package puni.zygarde.di

import java.lang.reflect.Method
import org.springframework.asm.Type
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.cglib.core.ClassGenerator
import org.springframework.cglib.core.DefaultGeneratorStrategy
import org.springframework.cglib.core.SpringNamingPolicy
import org.springframework.cglib.proxy.Enhancer
import org.springframework.cglib.proxy.MethodInterceptor
import org.springframework.cglib.proxy.MethodProxy
import org.springframework.cglib.transform.TransformingClassGenerator
import org.springframework.cglib.transform.impl.AddPropertyTransformer
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class DiServiceBeanPostProcessor(
  @Autowired val ctx: ApplicationContext
) : BeanPostProcessor {

  class BeanMethodInterceptor : MethodInterceptor {
    override fun intercept(target: Any?, method: Method, args: Array<out Any>?, proxy: MethodProxy): Any {
      return proxy.invokeSuper(target, args)
    }
  }

  override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
    if (bean is DiService && !beanName.contains("EnhancerBySpringCGLIB")) {
      val enhancer = Enhancer()
      enhancer.setSuperclass(bean.javaClass)
      enhancer.setInterfaces(bean.javaClass.interfaces)
      enhancer.strategy = object : DefaultGeneratorStrategy() {
        override fun transform(cg: ClassGenerator?): ClassGenerator? {
          return TransformingClassGenerator(
            cg,
            AddPropertyTransformer(
              mapOf(
                DiService.diCtxName to Type.getType(ApplicationContext::class.java)
              )
            )
          )
        }
      }
      enhancer.setUseFactory(false)
      enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE)
      enhancer.setCallbackTypes(arrayOf(BeanMethodInterceptor::class.java))
      enhancer.setCallbackFilter { m -> 0 }
      val clazz = enhancer.createClass()
      return ctx.autowireCapableBeanFactory
        .createBean(clazz)
        .also {
          val field = it.javaClass.getDeclaredField(DiService.diCtxFieldName)
          field.isAccessible = true
          field.set(it, ctx)
        }
    }
    return bean
  }
}
