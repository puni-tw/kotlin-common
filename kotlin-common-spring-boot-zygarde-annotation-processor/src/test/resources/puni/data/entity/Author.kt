package puni.data.entity

import puni.data.entity.AuthorApiSpec.Companion.DTO_AUTHOR
import puni.data.entity.AuthorApiSpec.Companion.DTO_AUTHOR_DETAIL
import puni.data.entity.BookApiSpec.Companion.DTO_BOOK
import puni.zygarde.api.AdditionalDtoProp
import puni.zygarde.api.AdditionalDtoProps
import puni.zygarde.api.ApiProp
import puni.zygarde.api.Dto
import puni.zygarde.api.ZygardeModel
import puni.zygarde.api.value.AutoIdValueProvider
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.MappedSuperclass
import javax.persistence.OneToMany


@MappedSuperclass
abstract class FooEntity: AutoIdEntity() {
  var createdAt: LocalDateTime? = null
}

@Entity
@AdditionalDtoProps(
  [
    AdditionalDtoProp(
      forDto = [DTO_AUTHOR, DTO_AUTHOR_DETAIL],
      field = "id",
      fieldType = Long::class,
      comment = "id of Author",
      entityValueProvider = AutoIdValueProvider::class
    )
  ]
)
@ZygardeModel
class Author(
  @ApiProp(
    dto = [Dto(DTO_AUTHOR), Dto(DTO_AUTHOR_DETAIL)]
  )
  var name: String = "",
  @ApiProp(
    dto = [Dto(DTO_AUTHOR), Dto(DTO_AUTHOR_DETAIL)]
  )
  var country: String = "",
  @ApiProp(
    dto = [Dto(DTO_AUTHOR_DETAIL, ref = DTO_BOOK, refCollection = true)]
  )
  @OneToMany(targetEntity = Book::class, mappedBy = "author")
  val books: MutableSet<Book> = mutableSetOf()
) : FooEntity()
