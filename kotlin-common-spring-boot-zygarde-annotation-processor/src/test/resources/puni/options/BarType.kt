package puni.options

import puni.zygarde.option.OptionEnum
import puni.zygarde.option.StaticOptionApi

@StaticOptionApi(comment = "bar bar")
enum class BarType(
  override val label: String
): OptionEnum {
  LOL("lol"),
  QAQ("qaq")
}
