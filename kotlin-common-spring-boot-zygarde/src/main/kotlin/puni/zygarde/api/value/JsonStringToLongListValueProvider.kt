package puni.zygarde.api.value

import puni.extension.jackson.jsonStringToList

class JsonStringToLongListValueProvider : ValueProvider<String, List<Long>> {
  override fun getValue(v: String): List<Long> {
    return v.jsonStringToList()
  }
}
