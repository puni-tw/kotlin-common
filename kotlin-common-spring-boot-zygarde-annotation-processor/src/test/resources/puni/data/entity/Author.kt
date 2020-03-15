package puni.data.entity

import puni.zygarde.api.ApiProp
import puni.zygarde.api.Dto
import javax.persistence.Entity

@Entity
class Author(
  @ApiProp(
    dto = [Dto()]
  )
  var name: String = "",
  @ApiProp(
    dto = [Dto()]
  )
  var country: String = ""
) : AutoIdEntity()
