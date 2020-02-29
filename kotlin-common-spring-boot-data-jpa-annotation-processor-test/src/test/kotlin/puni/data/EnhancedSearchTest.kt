package puni.data

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import puni.data.dao.AuthorDao
import puni.data.dao.BookDao
import puni.data.entity.Author
import puni.data.entity.AuthorFields
import puni.data.entity.Book
import puni.data.entity.BookFields
import puni.data.search.search
import puni.test.support.SpringTestSupport

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class EnhancedSearchTest : SpringTestSupport() {
  val authorDao: AuthorDao by autowired()
  val bookDao: BookDao by autowired()
  @Test
  fun `should able to do equal search`() {
    val author = authorDao.save(Author(name = "puni"))
    bookDao.save(Book(bookName = "book1", author = author))

    bookDao.search {
      it.field(BookFields.bookName).eq("book1")
    }.size shouldBe 1

    bookDao
      .search { search ->
        search
          .field(BookFields.author).field(AuthorFields.name)
          .eq("puni")
          .field(BookFields.bookName)
          .eq("book1")
      }
      .size shouldBe 1
  }
}
