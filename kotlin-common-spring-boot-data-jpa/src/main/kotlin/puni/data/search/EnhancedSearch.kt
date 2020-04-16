package puni.data.search

interface EnhancedSearch<EntityType> {

  fun <FieldType> field(
    fieldName: String
  ): ConditionAction<EntityType, EntityType, FieldType>

  fun <FieldType : Comparable<FieldType>> comparableField(
    fieldName: String
  ): ComparableConditionAction<EntityType, EntityType, FieldType>

  fun stringField(
    fieldName: String
  ): StringConditionAction<EntityType, EntityType>

  fun <FieldType> field(
    searchable: Searchable<EntityType, FieldType>
  ): ConditionAction<EntityType, EntityType, FieldType>

  fun <FieldType : Comparable<FieldType>> field(
    searchable: Searchable<EntityType, FieldType>
  ): ComparableConditionAction<EntityType, EntityType, FieldType>

  fun field(searchable: Searchable<EntityType, String>): StringConditionAction<EntityType, EntityType>

  fun or(searchContent: EnhancedSearch<EntityType>.() -> Unit): EnhancedSearch<EntityType>
}
