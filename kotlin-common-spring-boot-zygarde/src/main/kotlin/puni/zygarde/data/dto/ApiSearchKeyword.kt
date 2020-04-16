package puni.zygarde.data.dto

import io.swagger.annotations.ApiModel
import puni.data.search.SearchKeyword
import puni.data.search.SearchKeywordType

@ApiModel
class ApiSearchKeyword(
  keyword: String? = null,
  type: SearchKeywordType = SearchKeywordType.STARTS_WITH
) : SearchKeyword(keyword, type)
