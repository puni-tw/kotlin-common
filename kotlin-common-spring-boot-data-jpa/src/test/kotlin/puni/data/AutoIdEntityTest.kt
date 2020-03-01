package puni.data

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.util.Objects
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import puni.data.dao.TestAuthorDao
import puni.data.dao.TestBarDao
import puni.data.dao.TestBookDao
import puni.data.entity.Author
import puni.data.entity.Bar
import puni.data.entity.Book
import puni.data.entity.Foo
import puni.data.entity.getId

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [PuniSpringBootDataJpaTestApplication::class])
@ActiveProfiles("test")
@DirtiesContext
class AutoIdEntityTest(
  val testAuthorDao: TestAuthorDao,
  val testBookDao: TestBookDao,
  val testBarDao: TestBarDao
) : StringSpec({
  val author = testAuthorDao.save(Author())

  "should able to save and increase id" {
    val book = testBookDao.save(Book(author = author)).also {
      it.id shouldBe 1
    }
    val bar = testBarDao.save(Bar()).also {
      it.id shouldBe 1
    }
    Objects.equals(book, null) shouldBe false
    Objects.equals(book, bar) shouldBe false
    Objects.equals(book, Foo(id = 1, name = "")) shouldBe false
  }
  "should able to save list and increase id" {
    val books = testBookDao.saveAll(
      setOf(Book(author = author), Book(author = author))
    )
    books[0].getId() + 1 shouldBe books[1].getId()

    (books[0] == books[1]) shouldBe false
    books.toSet().size shouldBe 2

    (Book() == books[0]) shouldBe false
  }
})
