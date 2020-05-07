package puni.data.search

import javax.persistence.criteria.Predicate
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import puni.data.search.impl.EnhancedSearchImpl

private fun <T> buildSpec(searchContent: EnhancedSearch<T>.() -> Unit): Specification<T> {
  val predicates = mutableListOf<Predicate>()
  return Specification<T> { root, query, cb ->
    val enhancedSearchImpl = EnhancedSearchImpl(predicates, root, query, cb)
    searchContent.invoke(enhancedSearchImpl)
    if (enhancedSearchImpl.orders.isNotEmpty()) {
      query.orderBy(enhancedSearchImpl.orders)
    }
    cb.and(*predicates.toTypedArray())
  }
}

fun <T> JpaSpecificationExecutor<T>.search(searchContent: EnhancedSearch<T>.() -> Unit): List<T> {
  return findAll(buildSpec(searchContent))
}

fun <T> JpaSpecificationExecutor<T>.search(searchContent: EnhancedSearch<T>.() -> Unit, limit: Int): List<T> {
  return findAll(buildSpec(searchContent), PageRequest.of(0, limit)).content
}

fun <T> JpaSpecificationExecutor<T>.searchCount(searchContent: EnhancedSearch<T>.() -> Unit): Long {
  return count(buildSpec(searchContent))
}

fun <T> JpaSpecificationExecutor<T>.searchOne(searchContent: EnhancedSearch<T>.() -> Unit): T? {
  return findOne(buildSpec(searchContent)).let { if (it.isPresent) it.get() else null }
}

fun <T> JpaSpecificationExecutor<T>.searchPage(
  req: PagingAndSortingRequest,
  searchContent: EnhancedSearch<T>.() -> Unit
): Page<T> {
  return findAll(buildSpec(searchContent), req.paging.toPageRequest(req.sorting.asSort()))
}
