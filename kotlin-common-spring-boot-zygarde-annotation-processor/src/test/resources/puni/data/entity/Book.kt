package puni.data.entity

import puni.data.entity.BookApiSpec.Companion.DTO_BOOK
import puni.data.entity.BookApiSpec.Companion.DTO_BOOK_DETAIL
import puni.data.entity.BookApiSpec.Companion.REQ_BOOK_CREATE
import puni.data.entity.BookApiSpec.Companion.REQ_BOOK_SEARCH
import puni.data.search.PagingAndSortingRequest
import puni.data.search.SearchDateTimeRange
import puni.zygarde.api.AdditionalDtoProp
import puni.zygarde.api.AdditionalDtoProps
import puni.zygarde.api.ApiProp
import puni.zygarde.api.Dto
import puni.zygarde.api.DtoInherit
import puni.zygarde.api.DtoInherits
import puni.zygarde.api.RequestDto
import puni.zygarde.api.SearchType
import puni.zygarde.api.value.AutoIdValueProvider
import puni.zygarde.api.value.JsonStringToLongListValueProvider
import puni.zygarde.api.value.ToJsonStringValueProvider
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Lob
import javax.persistence.ManyToOne

@Entity
@AdditionalDtoProps(
  [
    AdditionalDtoProp(
      forDto = [DTO_BOOK, DTO_BOOK_DETAIL],
      field = "id",
      fieldType = Long::class,
      comment = "id of Book",
      entityValueProvider = AutoIdValueProvider::class
    )
  ]
)
@DtoInherits(
  [
    DtoInherit(dto = REQ_BOOK_SEARCH, inherit = PagingAndSortingRequest::class)
  ]
)
class Book(
  @ApiProp(
    dto = [Dto(DTO_BOOK), Dto(DTO_BOOK_DETAIL)],
    requestDto = [
      RequestDto(REQ_BOOK_CREATE),
      RequestDto(REQ_BOOK_SEARCH, searchType = SearchType.EQ)
    ],
    comment = "name of book"
  )
  var name: String = "",

  @ApiProp(
    dto = [Dto(DTO_BOOK), Dto(DTO_BOOK_DETAIL)],
    requestDto = [RequestDto(REQ_BOOK_CREATE), RequestDto(REQ_BOOK_SEARCH, searchType = SearchType.GT)],
    comment = "price of book"
  )
  var price: Int = 0,

  @ApiProp(dto = [Dto(DTO_BOOK_DETAIL)], requestDto = [RequestDto(REQ_BOOK_SEARCH, searchType = SearchType.LT)])
  var priceD: Double? = null,

  @ApiProp(dto = [Dto(DTO_BOOK_DETAIL)], requestDto = [RequestDto(REQ_BOOK_SEARCH, searchType = SearchType.GTE)])
  var priceF: Float? = null,

  @ApiProp(dto = [Dto(DTO_BOOK_DETAIL)], requestDto = [RequestDto(REQ_BOOK_SEARCH, searchType = SearchType.LTE)])
  var priceS: Short? = null,

  @ApiProp(
    dto = [
      Dto(DTO_BOOK, ref = AuthorApiSpec.DTO_AUTHOR),
      Dto(DTO_BOOK_DETAIL, ref = AuthorApiSpec.DTO_AUTHOR)
    ],
    comment = "author of book"
  )
  @ManyToOne(targetEntity = Author::class)
  var author: Author? = null,

  @ApiProp(
    dto = [
      Dto(DTO_BOOK),
      Dto(name = DTO_BOOK_DETAIL)
    ],
    requestDto = [
      RequestDto(REQ_BOOK_SEARCH, refClass = SearchDateTimeRange::class, searchType = SearchType.DATE_TIME_RANGE)
    ]
  )
  var releaseAt: LocalDateTime = LocalDateTime.now(),

  @ApiProp(
    dto = [
      Dto(
        name = DTO_BOOK_DETAIL,
        refClass = String::class,
        refCollection = true,
        entityValueProvider = BookTagsValueProvider::class
      )
    ],
    requestDto = [
      RequestDto(
        REQ_BOOK_CREATE,
        refClass = String::class,
        refCollection = true,
        valueProvider = ToJsonStringValueProvider::class
      )
    ]
  )
  @Lob
  var tags: String,
  @ApiProp(
    dto = [
      Dto(
        name = DTO_BOOK_DETAIL,
        refClass = Long::class,
        refCollection = true,
        valueProvider = JsonStringToLongListValueProvider::class
      )
    ]
  )
  @Lob
  var categoryIds: String
) : AutoIdEntity() {

  @ApiProp(
    requestDto = [
      RequestDto(REQ_BOOK_SEARCH, refClass = String::class, refCollection = true, searchType = SearchType.IN_LIST, searchForField = "name")
    ]
  )
  @Transient
  var names: Set<String>? = null
}
