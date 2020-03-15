package puni.zygarde.api.value

import puni.extension.jackson.toJsonString

class ToJsonStringValueProvider : ValueProvider<Any, String> {
  override fun getValue(v: Any): String = v.toJsonString()
}
