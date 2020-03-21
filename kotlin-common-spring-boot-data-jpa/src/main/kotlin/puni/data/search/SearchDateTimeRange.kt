package puni.data.search

import java.time.LocalDateTime

/**
 * @author leo
 */
data class SearchDateTimeRange(
  val from: LocalDateTime? = null,
  val until: LocalDateTime? = null
)
