package puni.zygarde.data.dto

import io.swagger.annotations.ApiModel
import java.time.LocalDateTime
import puni.data.search.SearchDateTimeRange

@ApiModel
class ApiSearchDateTimeRange(
  from: LocalDateTime? = null,
  until: LocalDateTime? = null
) : SearchDateTimeRange(from, until)
