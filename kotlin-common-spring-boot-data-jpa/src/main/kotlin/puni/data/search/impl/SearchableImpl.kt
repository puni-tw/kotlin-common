package puni.data.search.impl

import puni.data.search.Searchable

class SearchableImpl<F, T>(
  val fieldName: String
) : Searchable<F, T> {

  override fun fieldName(): String = fieldName
}
