package puni.data.entity

import puni.zygarde.api.AdditionalDtoProp
import puni.zygarde.api.AdditionalDtoProps
import puni.zygarde.api.ApiPathVariable
import puni.zygarde.api.ApiProp
import puni.zygarde.api.AutoIdValueProvider
import puni.zygarde.api.Dto
import puni.zygarde.api.GenApi
import puni.zygarde.api.ZgardeApi
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Lob
import javax.persistence.ManyToOne

@Entity
@ZgardeApi(
  [
    GenApi(
      path = "/api/author/{authorId}/book",
      apiName = "AuthorApi",
      apiOperation = "createBook",
      apiDescription = "create a book to author",
      pathVariable = [
        ApiPathVariable("authorId", Long::class)
      ]
    )
  ]
)
@AdditionalDtoProps(
  [
    AdditionalDtoProp(
      forDto = ["", "Detail"],
      field = "id",
      fieldType = Long::class,
      comment = "id of Book",
      valueProvider = AutoIdValueProvider::class
    )
  ]
)
class Book(
  @ApiProp(
    dto = [
      Dto(),
      Dto(name = "Detail")
    ],
    comment = "name of book"
  )
  var name: String = "",
  @ApiProp(
    dto = [
      Dto(),
      Dto(name = "Detail")
    ],
    comment = "price of book"
  )
  var price: Int = 0,
  @ApiProp(
    dto = [
      Dto(name = "Detail")
    ]
  )
  var priceD: Double? = null,
  @ApiProp(
    dto = [
      Dto(name = "Detail")
    ]
  )
  var priceF: Float? = null,
  @ApiProp(
    dto = [
      Dto(name = "Detail")
    ]
  )
  var priceS: Short? = null,
  @ApiProp(
    dto = [
      Dto(ref = "AuthorDto"),
      Dto(name = "Detail", ref = "AuthorDto")
    ],
    comment = "author of book"
  )
  @ManyToOne(targetEntity = Author::class)
  var author: Author = Author(),
  @ApiProp(
    dto = [
      Dto(),
      Dto(name = "Detail")
    ]
  )
  var releaseAt: LocalDateTime = LocalDateTime.now(),

  @ApiProp(
    dto = [
      Dto(
        name = "Detail",
        refClass = String::class,
        refCollection = true,
        valueProvider = BookTagsValueProvider::class
      )
    ]
  )
  @Lob
  var tags: String
) : AutoIdEntity()
