package puni.data.search

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

/**
 * @author leo
 */
data class PagingRequest(
  var page: Int = 1,
  var pageSize: Int = 10
)

fun PagingRequest.toPageRequest(sort: Sort = Sort.unsorted()): PageRequest {
  return PageRequest.of(
    page - 1,
    pageSize,
    sort
  )
}
