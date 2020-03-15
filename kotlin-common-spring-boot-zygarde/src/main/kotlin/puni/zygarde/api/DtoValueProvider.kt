package puni.zygarde.api

interface DtoValueProvider<E, T> {
  fun getValue(entity: E): T
}
