package puni.data.entity

import org.springframework.web.bind.annotation.RequestMethod
import puni.zygarde.api.AdditionalDtoProp
import puni.zygarde.api.AdditionalDtoProps
import puni.zygarde.api.ApiPathVariable
import puni.zygarde.api.ApiProp
import puni.zygarde.api.Dto
import puni.zygarde.api.GenApi
import puni.zygarde.api.ZygardeApi
import puni.zygarde.api.value.AutoIdValueProvider
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
@ZygardeApi(
  [
    GenApi(
      method = RequestMethod.GET,
      path = "/api/author",
      pathVariable = [],
      apiName = "AuthorApi",
      apiOperation = "getAuthors",
      apiDescription = "get all authors",
      serviceName = "AuthorService",
      serviceMethod = "getAllAuthors",
      reqRef = "",
      resRef = "AuthorDto",
      resCollection = true
    ),
    GenApi(
      method = RequestMethod.GET,
      path = "/api/author/{authorId}",
      pathVariable = [
        ApiPathVariable("authorId", Long::class)
      ],
      apiName = "AuthorApi",
      apiOperation = "getAuthor",
      apiDescription = "get author detail",
      serviceName = "AuthorService",
      serviceMethod = "getAuthorDetail",
      reqRef = "",
      resRef = "AuthorDetailDto",
      resCollection = false
    )
  ]
)
@AdditionalDtoProps(
  [
    AdditionalDtoProp(
      forDto = ["", "AuthorDetailDto"],
      field = "id",
      fieldType = Long::class,
      comment = "id of Author",
      valueProvider = AutoIdValueProvider::class
    )
  ]
)
class Author(
  @ApiProp(
    dto = [Dto(), Dto(name = "AuthorDetailDto")]
  )
  var name: String = "",
  @ApiProp(
    dto = [Dto(), Dto(name = "AuthorDetailDto")]
  )
  var country: String = "",
  @ApiProp(
    dto = [Dto(name = "AuthorDetailDto", ref = "BookDto", refCollection = true)]
  )
  @OneToMany(targetEntity = Book::class, mappedBy = "author")
  val books: MutableSet<Book> = mutableSetOf()
) : AutoIdEntity()
