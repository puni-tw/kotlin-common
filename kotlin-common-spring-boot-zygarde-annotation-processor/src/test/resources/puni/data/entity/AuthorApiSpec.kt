package puni.data.entity

import org.springframework.web.bind.annotation.RequestMethod
import puni.data.entity.AuthorApiSpec.Companion.API_AUTHOR
import puni.data.entity.AuthorApiSpec.Companion.DTO_AUTHOR
import puni.data.entity.AuthorApiSpec.Companion.DTO_AUTHOR_DETAIL
import puni.data.entity.AuthorApiSpec.Companion.SERVICE_AUTHOR
import puni.zygarde.api.ApiPathVariable
import puni.zygarde.api.GenApi
import puni.zygarde.api.ZygardeApi

@ZygardeApi(
  [
    GenApi(
      method = RequestMethod.GET,
      path = "/api/author",
      pathVariable = [],
      api = "${API_AUTHOR}.getAuthors",
      apiDescription = "get all authors",
      service = "${SERVICE_AUTHOR}.getAllAuthors",
      reqRef = "",
      resRef = DTO_AUTHOR,
      resCollection = true
    ),
    GenApi(
      method = RequestMethod.GET,
      path = "/api/author/{authorId}",
      pathVariable = [
        ApiPathVariable("authorId", Long::class)
      ],
      api = "${API_AUTHOR}.getAuthor",
      apiDescription = "get author detail",
      service = "${SERVICE_AUTHOR}.getAuthorDetail",
      reqRef = "",
      resRef = DTO_AUTHOR_DETAIL,
      resCollection = false
    )
  ]
)
interface AuthorApiSpec {
  companion object {
    const val API_AUTHOR = "AuthorApi"
    const val SERVICE_AUTHOR = "AuthorService"
    const val DTO_AUTHOR = "AuthorDto"
    const val DTO_AUTHOR_DETAIL = "AuthorDetailDto"
  }
}
