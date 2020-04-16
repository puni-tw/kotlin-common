package puni.data.search

interface ConditionAction<RootEntityType, EntityType, FieldType> {
  fun <AnotherFieldType> field(
    searchable: Searchable<FieldType, AnotherFieldType>
  ): ConditionAction<RootEntityType, FieldType, AnotherFieldType>

  fun <AnotherFieldType : Comparable<AnotherFieldType>> field(
    searchable: Searchable<FieldType, AnotherFieldType>
  ): ComparableConditionAction<RootEntityType, FieldType, AnotherFieldType>

  fun field(
    searchable: Searchable<FieldType, String>
  ): StringConditionAction<RootEntityType, FieldType>

  infix fun eq(value: FieldType?): EnhancedSearch<RootEntityType>

  infix fun notEq(value: FieldType?): EnhancedSearch<RootEntityType>

  infix fun inList(values: Collection<FieldType>?): EnhancedSearch<RootEntityType>

  infix fun notInList(values: Collection<FieldType>?): EnhancedSearch<RootEntityType>

  fun isNotNull(): EnhancedSearch<RootEntityType>

  fun isNull(): EnhancedSearch<RootEntityType>
}
