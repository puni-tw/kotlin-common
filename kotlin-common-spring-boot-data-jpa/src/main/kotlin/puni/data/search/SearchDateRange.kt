package puni.data.search

import java.time.LocalDate

/**
 * @author leo
 */
data class SearchDateRange(
  val from: LocalDate? = null,
  val to: LocalDate? = null
)
