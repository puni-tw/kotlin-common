package puni.data.entity

import puni.zygarde.api.ApiProp
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class Book(
  @ApiProp
  var name: String = "",
  @ApiProp
  var price: Int = 0,
  @ApiProp
  var priceD: Double? = null,
  @ApiProp
  var priceF: Float? = null,
  @ApiProp
  var priceS: Short? = null,
  @ManyToOne(targetEntity = Author::class)
  var author: Author = Author(),
  @ApiProp
  var releaseAt: LocalDateTime = LocalDateTime.now()
) : AutoIdEntity()
