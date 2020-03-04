package puni.data.search

interface ComparableConditionAction<RootEntityType, EntityType, FieldType : Comparable<FieldType>> :
  ConditionAction<RootEntityType, EntityType, FieldType> {
  infix fun gt(value: FieldType?): EnhancedSearch<RootEntityType>
  infix fun gte(value: FieldType?): EnhancedSearch<RootEntityType>
  infix fun lt(value: FieldType?): EnhancedSearch<RootEntityType>
  infix fun lte(value: FieldType?): EnhancedSearch<RootEntityType>
}
