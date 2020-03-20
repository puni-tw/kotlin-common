package puni.data.entity

import org.springframework.web.bind.annotation.RequestMethod
import puni.zygarde.api.AdditionalDtoProp
import puni.zygarde.api.AdditionalDtoProps
import puni.zygarde.api.ApiPathVariable
import puni.zygarde.api.ApiProp
import puni.zygarde.api.Dto
import puni.zygarde.api.GenApi
import puni.zygarde.api.RequestDto
import puni.zygarde.api.ZygardeApi
import puni.zygarde.api.value.AutoIdValueProvider
import puni.zygarde.api.value.ToJsonStringValueProvider
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Lob
import javax.persistence.ManyToOne

@Entity
@ZygardeApi(
  [
    GenApi(
      method = RequestMethod.POST,
      path = "/api/author/{authorId}/book",
      pathVariable = [
        ApiPathVariable("authorId", Long::class)
      ],
      apiName = "AuthorApi",
      apiOperation = "createBook",
      apiDescription = "create a book to author",
      serviceName = "AuthorService",
      serviceMethod = "createBook",
      reqRef = "BookCreateRequest",
      resRef = "BookDetailDto"
    )
  ]
)
@AdditionalDtoProps(
  [
    AdditionalDtoProp(
      forDto = ["", "BookDetailDto"],
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
      Dto(name = "BookDetailDto")
    ],
    requestDto = [
      RequestDto("BookCreateRequest")
    ],
    comment = "name of book"
  )
  var name: String = "",

  @ApiProp(
    dto = [
      Dto(),
      Dto(name = "BookDetailDto")
    ],
    requestDto = [
      RequestDto("BookCreateRequest")
    ],
    comment = "price of book"
  )
  var price: Int = 0,

  @ApiProp(
    dto = [
      Dto(name = "BookDetailDto")
    ]
  )
  var priceD: Double? = null,

  @ApiProp(
    dto = [
      Dto(name = "BookDetailDto")
    ]
  )
  var priceF: Float? = null,

  @ApiProp(
    dto = [
      Dto(name = "BookDetailDto")
    ]
  )
  var priceS: Short? = null,

  @ApiProp(
    dto = [
      Dto(ref = "AuthorDto"),
      Dto(name = "BookDetailDto", ref = "AuthorDto")
    ],
    comment = "author of book"
  )
  @ManyToOne(targetEntity = Author::class)
  var author: Author = Author(),

  @ApiProp(
    dto = [
      Dto(),
      Dto(name = "BookDetailDto")
    ]
  )
  var releaseAt: LocalDateTime = LocalDateTime.now(),

  @ApiProp(
    dto = [
      Dto(
        name = "BookDetailDto",
        refClass = String::class,
        refCollection = true,
        valueProvider = BookTagsValueProvider::class
      )
    ],
    requestDto = [
      RequestDto(
        "BookCreateRequest",
        refClass = String::class,
        refCollection = true,
        valueProvider = ToJsonStringValueProvider::class
      )
    ]
  )
  @Lob
  var tags: String
) : AutoIdEntity()
