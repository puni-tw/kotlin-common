package puni.data.search

interface ComparableConditionAction<RootEntityType, EntityType, FieldType : Comparable<FieldType>> :
  ConditionAction<RootEntityType, EntityType, FieldType> {
  fun gt(value: FieldType?): EnhancedSearch<RootEntityType>
  fun gte(value: FieldType?): EnhancedSearch<RootEntityType>
  fun lt(value: FieldType?): EnhancedSearch<RootEntityType>
  fun lte(value: FieldType?): EnhancedSearch<RootEntityType>
}
