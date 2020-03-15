package puni.zygarde.api

class NoOpValueProvider : DtoValueProvider<Any, Any> {
  override fun getValue(entity: Any): Any = throw AssertionError()
}
