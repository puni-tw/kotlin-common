package puni.zygarde.api

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class ApiProp(
  val comment: String = "",
  val dto: Array<Dto> = []
)
