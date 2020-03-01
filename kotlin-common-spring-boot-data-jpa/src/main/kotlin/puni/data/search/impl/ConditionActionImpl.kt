package puni.data.search.impl

import javax.persistence.criteria.Path
import javax.persistence.criteria.Root
import puni.data.search.ComparableConditionAction
import puni.data.search.ConditionAction
import puni.data.search.EnhancedSearch
import puni.data.search.Searchable

open class ConditionActionImpl<RootEntityType, EntityType, FieldType>(
  val enhancedSearch: EnhancedSearchImpl<RootEntityType>,
  val columnName: String
) : ConditionAction<RootEntityType, EntityType, FieldType> {

  override fun <AnotherFieldType> field(
    searchable: Searchable<FieldType, AnotherFieldType>
  ): ConditionAction<RootEntityType, FieldType, AnotherFieldType> {
    return ConditionActionImpl<RootEntityType, FieldType, AnotherFieldType>(
      enhancedSearch, "$columnName.${searchable.fieldName()}"
    )
  }

  override fun <AnotherFieldType : Comparable<AnotherFieldType>> field(
    searchable: Searchable<FieldType, AnotherFieldType>
  ): ComparableConditionAction<RootEntityType, FieldType, AnotherFieldType> {
    return ComparableConditionActionImpl<RootEntityType, FieldType, AnotherFieldType>(
      enhancedSearch, "$columnName.${searchable.fieldName()}"
    )
  }

  override fun eq(value: FieldType?): EnhancedSearch<RootEntityType> = enhancedSearch.apply {
    value?.let {
      predicates.add(cb.equal(root.columnNameToPath(columnName), it))
    }
  }

  override fun notEq(value: FieldType?): EnhancedSearch<RootEntityType> = enhancedSearch.apply {
    value?.let {
      predicates.add(cb.notEqual(root.columnNameToPath(columnName), it))
    }
  }

  protected fun Root<RootEntityType>.columnNameToPath(columnName: String): Path<FieldType> {
    val splited = columnName.split(".")
    return splited.takeLast(splited.size - 1)
      .fold(
        this.get<FieldType>(splited.first()),
        { path, column -> path.get(column) }
      )
  }
}
