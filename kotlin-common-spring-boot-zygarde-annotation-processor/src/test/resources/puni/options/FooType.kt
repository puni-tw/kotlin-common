package puni.options

import puni.zygarde.option.OptionEnum
import puni.zygarde.option.StaticOptionApi

@StaticOptionApi(comment = "foo bar")
enum class FooType(
  override val label: String
): OptionEnum {
  FOO("foo"),
  BAR("bar")
}
