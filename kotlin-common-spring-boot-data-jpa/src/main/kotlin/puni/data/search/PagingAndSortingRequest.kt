package puni.data.search

open class PagingAndSortingRequest {
  var paging: PagingRequest = PagingRequest()
  var sorting: SortingRequest? = null
}
