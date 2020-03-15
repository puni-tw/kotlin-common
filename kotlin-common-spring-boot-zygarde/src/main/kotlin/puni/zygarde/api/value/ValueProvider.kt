package puni.zygarde.api.value

interface ValueProvider<E, T> {
  fun getValue(v: E): T
}
