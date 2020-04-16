package puni.data.search.impl

import puni.data.search.ComparableConditionAction
import puni.data.search.EnhancedSearch

open class ComparableConditionActionImpl<RootEntityType, EntityType, FieldType : Comparable<FieldType>>(
  enhancedSearch: EnhancedSearchImpl<RootEntityType>,
  columnName: String
) : ConditionActionImpl<RootEntityType, EntityType, FieldType>(enhancedSearch, columnName),
  ComparableConditionAction<RootEntityType, EntityType, FieldType> {

  override fun gt(value: FieldType?): EnhancedSearch<RootEntityType> = applyNonNullAction(value) { path, v ->
    cb.greaterThan(path, v)
  }

  override fun gte(value: FieldType?): EnhancedSearch<RootEntityType> = applyNonNullAction(value) { path, v ->
    cb.greaterThanOrEqualTo(path, v)
  }

  override fun lt(value: FieldType?): EnhancedSearch<RootEntityType> = applyNonNullAction(value) { path, v ->
    cb.lessThan(path, v)
  }

  override fun lte(value: FieldType?): EnhancedSearch<RootEntityType> = applyNonNullAction(value) { path, v ->
    cb.lessThanOrEqualTo(path, v)
  }
}
