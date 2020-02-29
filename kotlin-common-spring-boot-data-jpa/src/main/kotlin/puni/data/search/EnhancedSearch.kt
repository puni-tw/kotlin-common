package puni.data.search

interface EnhancedSearch<EntityType> {

  fun <FieldType> field(searchable: Searchable<EntityType, FieldType>): ConditionAction<EntityType, EntityType, FieldType>
}
