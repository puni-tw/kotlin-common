package puni.zygarde.api

import org.springframework.web.bind.annotation.RequestMethod

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GenApi(
  val path: String,
  val apiName: String,
  val apiOperation: String,
  val apiDescription: String = "",
  val pathVariable: Array<ApiPathVariable> = [],
  val method: RequestMethod = RequestMethod.POST
)
