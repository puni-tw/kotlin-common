package puni.data

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import puni.data.dao.AuthorDao
import puni.data.dao.BookDao
import puni.data.entity.Author
import puni.data.entity.Book
import puni.data.service.BookService
import puni.test.support.SpringTestSupport

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class EnhancedSearchTest : SpringTestSupport() {
  val authorDao: AuthorDao by autowired()
  val bookDao: BookDao by autowired()
  val bookService: BookService by autowired()
  @Test
  fun `should able to do equal search`() {
    val author = authorDao.save(Author(name = "puni"))
    bookDao.save(Book(name = "book1", author = author))

    bookService.countByName("book1") shouldBe 1
    bookService.countByNameAndAuthorName("book1", "puni") shouldBe 1
  }
}
