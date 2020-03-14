package puni.zygarde.api

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GenerateUpdateApi(
  val path: String = "",
  val pathVariable: Array<ApiPathVariable> = []
)
