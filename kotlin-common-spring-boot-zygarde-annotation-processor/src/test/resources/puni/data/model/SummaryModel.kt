package puni.data.model

import org.springframework.web.bind.annotation.RequestMethod
import puni.zygarde.api.ApiProp
import puni.zygarde.api.Dto
import puni.zygarde.api.GenApi
import puni.zygarde.api.ZygardeApi
import puni.zygarde.api.ZygardeModel

const val DTO_SUMMARY = "SummaryDto"

@ZygardeApi(
  [GenApi(
    RequestMethod.GET,
    path = "/p/api/summary",
    api = "SummaryApi.getSummary",
    service = "SummaryService.getSummary",
    reqRef = "",
    resRef = DTO_SUMMARY
  )]
)
@ZygardeModel
class SummaryModel {
  @ApiProp(
    dto = [Dto(DTO_SUMMARY)]
  )
  val bookCountByAuthorMap: Map<Long, Int> = emptyMap()
}
