package puni.data.search

interface StringConditionAction<RootEntityType, EntityType> : ComparableConditionAction<RootEntityType, EntityType, String> {
  infix fun keyword(value: SearchKeyword?): EnhancedSearch<RootEntityType>
  infix fun startsWith(value: String?): EnhancedSearch<RootEntityType>
  infix fun endsWith(value: String?): EnhancedSearch<RootEntityType>
  infix fun contains(value: String?): EnhancedSearch<RootEntityType>
}
