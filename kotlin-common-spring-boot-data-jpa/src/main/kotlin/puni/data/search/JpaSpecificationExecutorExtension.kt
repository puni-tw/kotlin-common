package puni.data.search

import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import puni.data.search.impl.EnhancedSearchImpl

fun <T> JpaSpecificationExecutor<T>.search(searchContent: (enhancedSearch: EnhancedSearch<T>) -> Unit): List<T> {
  val predicates = mutableListOf<Predicate>()
  val specification: (Root<T>, CriteriaQuery<*>, CriteriaBuilder) -> Predicate = { root, query, cb ->
    searchContent.invoke(EnhancedSearchImpl(predicates, root, query, cb))
    cb.and(*predicates.toTypedArray())
  }
  return findAll(specification)
}
