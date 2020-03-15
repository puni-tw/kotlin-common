package puni.zygarde.api

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ZgardeApi(
  val api: Array<GenApi> = []
)
