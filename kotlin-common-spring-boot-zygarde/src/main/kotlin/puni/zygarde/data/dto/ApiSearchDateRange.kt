package puni.zygarde.data.dto

import io.swagger.annotations.ApiModel
import java.time.LocalDate
import puni.data.search.SearchDateRange

@ApiModel
class ApiSearchDateRange(
  from: LocalDate? = null,
  to: LocalDate? = null
) : SearchDateRange(from, to)
