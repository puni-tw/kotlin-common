package puni.zygarde.api

import kotlin.reflect.KClass
import puni.zygarde.api.value.NoOpValueProvider
import puni.zygarde.api.value.ValueProvider

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AdditionalDtoProp(
  val forDto: Array<String> = [],
  val field: String = "",
  val fieldType: KClass<*> = Any::class,
  val comment: String = "",
  val valueProvider: KClass<out ValueProvider<*, *>> = NoOpValueProvider::class,
  val entityValueProvider: KClass<out ValueProvider<*, *>> = NoOpValueProvider::class
)
