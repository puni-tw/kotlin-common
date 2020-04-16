package puni.data.search.impl

import puni.data.search.EnhancedSearch
import puni.data.search.SearchKeyword
import puni.data.search.SearchKeywordType
import puni.data.search.StringConditionAction

class StringConditionActionImpl<RootEntityType, EntityType>(
  enhancedSearch: EnhancedSearchImpl<RootEntityType>,
  columnName: String
) : ComparableConditionActionImpl<RootEntityType, EntityType, String>(enhancedSearch, columnName), StringConditionAction<RootEntityType, EntityType> {

  override fun keyword(value: SearchKeyword?): EnhancedSearch<RootEntityType> = applyNonNullAction(value?.keyword) { path, keyword ->
    when (value!!.type) {
      SearchKeywordType.CONTAINS -> cb.like(path, "%$keyword%")
      SearchKeywordType.STARTS_WITH -> cb.like(path, "$keyword%")
      SearchKeywordType.ENDS_WITH -> cb.like(path, "%$keyword")
      SearchKeywordType.MATCH -> cb.equal(path, keyword)
    }
  }

  override fun startsWith(value: String?): EnhancedSearch<RootEntityType> {
    return keyword(SearchKeyword(value, SearchKeywordType.STARTS_WITH))
  }

  override fun endsWith(value: String?): EnhancedSearch<RootEntityType> {
    return keyword(SearchKeyword(value, SearchKeywordType.ENDS_WITH))
  }

  override fun contains(value: String?): EnhancedSearch<RootEntityType> {
    return keyword(SearchKeyword(value, SearchKeywordType.CONTAINS))
  }
}
