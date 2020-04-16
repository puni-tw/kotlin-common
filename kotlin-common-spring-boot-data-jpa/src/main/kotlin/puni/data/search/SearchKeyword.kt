package puni.data.search

open class SearchKeyword(
  var keyword: String? = null,
  var type: SearchKeywordType = SearchKeywordType.STARTS_WITH
)
