package puni.zygarde.api

import org.springframework.web.bind.annotation.RequestMethod

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GenApi(
  val method: RequestMethod,
  val path: String,
  val pathVariable: Array<ApiPathVariable> = [],
  val apiName: String,
  val apiOperation: String,
  val apiDescription: String = "",
  val serviceName: String,
  val serviceMethod: String,
  val reqRef: String,
  val reqCollection: Boolean = false,
  val resRef: String,
  val resCollection: Boolean = false
)
