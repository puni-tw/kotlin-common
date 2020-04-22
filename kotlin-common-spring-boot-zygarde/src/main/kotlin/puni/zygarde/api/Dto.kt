package puni.zygarde.api

import kotlin.reflect.KClass
import puni.zygarde.api.value.NoOpValueProvider
import puni.zygarde.api.value.ValueProvider

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Dto(
  /**
   * Name of DTO in this field
   */
  val name: String,
  /**
   * override fieldName in DTO
   */
  val fieldName: String = "",
  /**
   * ref DTO Name
   */
  val ref: String = "",
  /**
   * specify if ref is Collection
   */
  val refCollection: Boolean = false,
  /**
   * specify ref Class if it's not DTO
   */
  val refClass: KClass<*> = Any::class,
  /**
   * whether assign value in dto from entity in generated toXXXDto extension function
   */
  val applyValueFromEntity: Boolean = true,
  val entityValueProvider: KClass<out ValueProvider<*, *>> = NoOpValueProvider::class,
  val valueProvider: KClass<out ValueProvider<*, *>> = NoOpValueProvider::class
)
