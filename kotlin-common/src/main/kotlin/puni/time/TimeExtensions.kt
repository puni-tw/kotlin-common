package puni.time

import java.time.LocalDateTime

fun LocalDateTime.midnight(): LocalDateTime {
  return this.toLocalDate().atStartOfDay().plusDays(1).minusNanos(1L)
}

fun LocalDateTime.startOfDay(): LocalDateTime {
  return this.toLocalDate().atStartOfDay()
}
