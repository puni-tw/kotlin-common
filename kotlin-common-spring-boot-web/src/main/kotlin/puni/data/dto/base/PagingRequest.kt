package puni.data.dto.base

import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel
@ApiImplicitParams(
  ApiImplicitParam("分頁(從1開始)", defaultValue = "1", name = "page"),
  ApiImplicitParam("每頁數量", defaultValue = "10", name = "pageSize")
)
data class PagingRequest(
  @ApiModelProperty(notes = "分頁(從1開始)", example = "1")
  var page: Int = 1,
  @ApiModelProperty(notes = "每頁數量", example = "10")
  var pageSize: Int = 10
)
