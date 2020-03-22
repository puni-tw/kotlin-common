package puni.zygarde.api.value

import puni.extension.jackson.jsonStringToList

class JsonStringToStringListValueProvider : ValueProvider<String, List<String>> {
  override fun getValue(v: String): List<String> {
    return v.jsonStringToList()
  }
}
