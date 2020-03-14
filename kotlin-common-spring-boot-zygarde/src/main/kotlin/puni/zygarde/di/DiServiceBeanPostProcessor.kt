package puni.zygarde.di

import java.lang.reflect.Method
import net.bytebuddy.ByteBuddy
import org.springframework.asm.Type
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.cglib.core.ClassGenerator
import org.springframework.cglib.core.DefaultGeneratorStrategy
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

  override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
    if (bean is DiService) {
      if (bean.javaClass.declaredFields.any { it.name == DiService.diCtxFieldName }) {
        println("$beanName already has ctx field")
        return bean
      }

      println("")
      println("")
      println(bean)
      println(beanName)

      val clazz = createClassByByteBuddy(bean)
      println("enhanced class $clazz")
      val beanFactory = ctx.autowireCapableBeanFactory
      return beanFactory
        .createBean(clazz)
        .also {
          println("bean class ${it.javaClass}")
          val field = it.javaClass.declaredFields.find { it.name == DiService.diCtxFieldName }
          if (field != null) {
            field.isAccessible = true
            field.set(it, ctx)
          }
        }
    }
    return bean
  }

  private fun createClassByByteBuddy(bean: Any): Class<Any> {
    return ByteBuddy()
      .subclass(bean.javaClass)
      .defineField(DiService.diCtxFieldName, ApplicationContext::class.java)
      .make()
      .load(bean.javaClass.classLoader)
      .loaded as Class<Any>
  }
  private fun createClassByCglib(bean: Any): Class<Any> {
    val enhancer = Enhancer()
    // enhancer.namingPolicy = object : DefaultNamingPolicy() {
    //   override fun getClassName(prefix: String?, source: String?, key: Any?, names: Predicate?): String {
    //     return super.getClassName(prefix, source, key, names).replace("$$", "_")
    //   }
    //
    //   override fun getTag(): String {
    //     return super.getTag() + "_DI"
    //   }
    // }
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
    // enhancer.namingPolicy = SpringNamingPolicy.INSTANCE
    // enhancer.setUseFactory(false)
    enhancer.setCallbackTypes(arrayOf(BeanMethodInterceptor::class.java))
    enhancer.setCallbackFilter { m -> 0 }
    val clazz = enhancer.createClass()
    return clazz
  }
}
