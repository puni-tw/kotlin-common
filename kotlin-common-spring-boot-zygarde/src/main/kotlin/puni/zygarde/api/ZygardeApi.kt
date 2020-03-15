package puni.zygarde.api

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ZygardeApi(
  val api: Array<GenApi> = []
)
