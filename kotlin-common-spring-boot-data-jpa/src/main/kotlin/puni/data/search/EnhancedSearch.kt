package puni.data.search

interface EnhancedSearch<EntityType> {

  fun <FieldType> field(
    fieldName: String
  ): ConditionAction<EntityType, EntityType, FieldType>

  fun <FieldType : Comparable<FieldType>> comparableField(
    fieldName: String
  ): ComparableConditionAction<EntityType, EntityType, FieldType>

  fun <FieldType> field(
    searchable: Searchable<EntityType, FieldType>
  ): ConditionAction<EntityType, EntityType, FieldType>

  fun <FieldType : Comparable<FieldType>> field(
    searchable: Searchable<EntityType, FieldType>
  ): ComparableConditionAction<EntityType, EntityType, FieldType>

  fun or(searchContent: (enhancedSearch: EnhancedSearch<EntityType>) -> Unit): EnhancedSearch<EntityType>
}
