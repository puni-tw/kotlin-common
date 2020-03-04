package puni.data.search

interface ConditionAction<RootEntityType, EntityType, FieldType> {
  fun <AnotherFieldType> field(
    searchable: Searchable<FieldType, AnotherFieldType>
  ): ConditionAction<RootEntityType, FieldType, AnotherFieldType>

  fun <AnotherFieldType : Comparable<AnotherFieldType>> field(
    searchable: Searchable<FieldType, AnotherFieldType>
  ): ComparableConditionAction<RootEntityType, FieldType, AnotherFieldType>

  fun eq(value: FieldType?): EnhancedSearch<RootEntityType>

  fun notEq(value: FieldType?): EnhancedSearch<RootEntityType>

  fun inList(values: Collection<FieldType>?): EnhancedSearch<RootEntityType>
}
