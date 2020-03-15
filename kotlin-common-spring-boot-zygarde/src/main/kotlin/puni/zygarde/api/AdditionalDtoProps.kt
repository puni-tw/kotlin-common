package puni.zygarde.api

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AdditionalDtoProps(
  val props: Array<AdditionalDtoProp> = []
)
