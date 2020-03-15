package puni.zygarde.api.value

class NoOpValueProvider : ValueProvider<Any, Any> {
  override fun getValue(v: Any): Any = throw AssertionError()
}
