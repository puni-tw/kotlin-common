package puni.zygarde.di

import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext

interface DiService {
  companion object {
    val cglibPrefix = "\$cglib_prop_"
    val diCtxName = "\$di_ctx"
    val diCtxFieldName = "$cglibPrefix$diCtxName"
  }
}

inline fun <reified T : Any> DiService.bean(): T {
  val ctx = this.javaClass.getDeclaredField(DiService.diCtxFieldName)
    .also { it.isAccessible = true }
    .get(this) as ApplicationContext
  return ctx.getBean<T>()
}

inline fun <reified T : Any> DiService.autowired(): Lazy<T> = lazy { bean<T>() }
