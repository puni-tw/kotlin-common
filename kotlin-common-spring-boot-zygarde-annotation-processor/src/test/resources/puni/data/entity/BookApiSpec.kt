package puni.data.entity

import org.springframework.web.bind.annotation.RequestMethod
import puni.zygarde.api.ApiPathVariable
import puni.zygarde.api.GenApi
import puni.zygarde.api.ZygardeApi

@ZygardeApi(
  [
    GenApi(
      method = RequestMethod.POST,
      path = "/api/author/{authorId}/book",
      pathVariable = [
        ApiPathVariable("authorId", Long::class)
      ],
      api = "AuthorApi.createBook",
      apiDescription = "create a book to author",
      service = "AuthorService.createBook",
      reqRef = "BookCreateRequest",
      resRef = BookApiSpec.DTO_BOOK_DETAIL
    )
  ]
)
interface BookApiSpec {
  companion object {
    const val DTO_BOOK = "BookDto"
    const val DTO_BOOK_DETAIL = "BookDetailDto"
    const val REQ_BOOK_CREATE = "BookCreateRequest"
    const val REQ_BOOK_SEARCH = "BookSearchRequest"
  }
}
