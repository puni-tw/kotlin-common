package puni.zygarde.api

enum class SearchType {
  NONE,
  EQ,
  NOT_EQ,
  LT,
  GT,
  LTE,
  GTE,
  IN_LIST,
  DATE_RANGE,
  DATE_TIME_RANGE,
}
