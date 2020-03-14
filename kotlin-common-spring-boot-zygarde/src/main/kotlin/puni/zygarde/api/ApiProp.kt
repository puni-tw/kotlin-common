package puni.zygarde.api

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class ApiProp(
  val create: Boolean = true,
  val update: Boolean = true,
  val comment: String = ""
)
