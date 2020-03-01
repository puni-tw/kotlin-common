package puni.data.search.impl

import puni.data.search.ComparableConditionAction
import puni.data.search.EnhancedSearch

class ComparableConditionActionImpl<RootEntityType, EntityType, FieldType : Comparable<FieldType>>(
  enhancedSearch: EnhancedSearchImpl<RootEntityType>,
  columnName: String
) : ConditionActionImpl<RootEntityType, EntityType, FieldType>(enhancedSearch, columnName),
  ComparableConditionAction<RootEntityType, EntityType, FieldType> {

  override fun gt(value: FieldType?): EnhancedSearch<RootEntityType> = enhancedSearch.apply {
    value?.let {
      predicates.add(cb.greaterThan(root.columnNameToPath(columnName), it))
    }
  }

  override fun gte(value: FieldType?): EnhancedSearch<RootEntityType> = enhancedSearch.apply {
    value?.let {
      predicates.add(cb.greaterThanOrEqualTo(root.columnNameToPath(columnName), it))
    }
  }

  override fun lt(value: FieldType?): EnhancedSearch<RootEntityType> = enhancedSearch.apply {
    value?.let {
      predicates.add(cb.lessThan(root.columnNameToPath(columnName), it))
    }
  }

  override fun lte(value: FieldType?): EnhancedSearch<RootEntityType> = enhancedSearch.apply {
    value?.let {
      predicates.add(cb.lessThanOrEqualTo(root.columnNameToPath(columnName), it))
    }
  }
}
