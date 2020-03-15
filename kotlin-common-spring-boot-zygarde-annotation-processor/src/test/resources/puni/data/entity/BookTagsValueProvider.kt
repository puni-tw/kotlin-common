package puni.data.entity

import puni.extension.jackson.jsonStringToList
import puni.zygarde.api.DtoValueProvider

class BookTagsValueProvider: DtoValueProvider<Book, Collection<String>> {
  override fun getValue(entity: Book): Collection<String> {
    return entity.tags.jsonStringToList()
  }
}
