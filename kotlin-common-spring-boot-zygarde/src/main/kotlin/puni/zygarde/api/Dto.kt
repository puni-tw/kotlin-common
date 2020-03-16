package puni.zygarde.api

import kotlin.reflect.KClass
import puni.zygarde.api.value.NoOpValueProvider
import puni.zygarde.api.value.ValueProvider

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Dto(
  val name: String = "",
  val refClass: KClass<*> = Any::class,
  val refCollection: Boolean = false,
  val ref: String = "",
  val applyValueFromEntity: Boolean = true,
  val valueProvider: KClass<out ValueProvider<*, *>> = NoOpValueProvider::class
)
