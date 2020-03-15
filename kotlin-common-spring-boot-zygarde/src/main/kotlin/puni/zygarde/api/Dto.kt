package puni.zygarde.api

import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Dto(
  val name: String = "",
  val refClass: KClass<*> = Any::class,
  val refCollection: Boolean = false,
  val ref: String = "",
  val valueProvider: KClass<out DtoValueProvider<*, *>> = NoOpValueProvider::class
)
