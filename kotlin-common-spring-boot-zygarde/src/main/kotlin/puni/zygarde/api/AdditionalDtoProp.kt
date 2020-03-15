package puni.zygarde.api

import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AdditionalDtoProp(
  val forDto: Array<String> = [],
  val field: String = "",
  val fieldType: KClass<*> = Any::class,
  val comment: String = "",
  val valueProvider: KClass<out DtoValueProvider<*, *>>
)
