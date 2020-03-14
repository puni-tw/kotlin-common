package puni.zygarde.option

annotation class StaticOptionApi(
  val comment: String = ""
)

@StaticOptionApi(comment = "foo bar")
enum class FooType(
  override val label: String
) : OptionEnum {
  FOO("foo"),
  BAR("bar")
}
