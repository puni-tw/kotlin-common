package puni.zygarde.option

interface OptionEnum {
  val name: String
  val label: String
  fun toOptionDto() = OptionDto(name, label)
}
