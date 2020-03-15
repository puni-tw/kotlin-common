package puni.data.entity

import puni.extension.jackson.jsonStringToList
import puni.zygarde.api.value.ValueProvider

class BookTagsValueProvider: ValueProvider<Book, Collection<String>> {
  override fun getValue(v: Book): Collection<String> = v.tags.jsonStringToList()
}
