package puni.data

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import puni.data.dao.TestAuthorDao
import puni.data.dao.TestBookDao
import puni.data.dao.TestGroupDao
import puni.data.entity.Author
import puni.data.entity.AuthorGroup
import puni.data.entity.Book
import puni.data.search.impl.SearchableImpl
import puni.data.search.search

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [PuniSpringBootDataJpaTestApplication::class])
@ActiveProfiles("test")
@DirtiesContext
class DaoSearchTest(
  val testBookDao: TestBookDao,
  val testGroupDao: TestGroupDao,
  val testAuthorDao: TestAuthorDao
) : StringSpec({
  "should able to do complex search" {
    testBookDao.save(
      Book(
        "puni",
        100,
        testAuthorDao.save(
          Author(
            "author",
            testGroupDao.save(AuthorGroup("puni"))
          )
        )
      )
    )
    testBookDao.search {
      field(SearchableImpl<Book, String>("name")).eq("puni")
      field<String>("name").eq("puni")
      field<String>("name").inList(null) // will be ignore
      field<String>("name").inList(emptyList()) // will be ignore
      field<String>("name").inList(listOf("puni"))
      field(SearchableImpl<Book, String>("name")).notEq("puni2")
      field(SearchableImpl<Book, String>("name")).eq(null) // will be ignored
      field(SearchableImpl<Book, String>("name")).notEq(null) // will be ignored

      or {
        field<String>("name").eq("puni")
        field<Int>("price").eq(99)
      }

      field(SearchableImpl<Book, Author>("author"))
        .field(SearchableImpl<Author, String>("name")).eq("author")

      field(SearchableImpl<Book, Author>("author"))
        .field(SearchableImpl<Author, AuthorGroup>("authorGroup"))
        .field(SearchableImpl<AuthorGroup, String>("name")).eq("puni")

      comparableField<Int>("price").gt(50)
      field(SearchableImpl<Book, Int>("price")).eq(100)
      field(SearchableImpl<Book, Int>("price")).gt(99)
      field(SearchableImpl<Book, Int>("price")).gt(null) // will be ignored
      field(SearchableImpl<Book, Int>("price")).lt(101)
      field(SearchableImpl<Book, Int>("price")).lt(null) // will be ignored
      field(SearchableImpl<Book, Int>("price")).gte(100)
      field(SearchableImpl<Book, Int>("price")).gte(null) // will be ignored
      field(SearchableImpl<Book, Int>("price")).lte(100)
      field(SearchableImpl<Book, Int>("price")).lte(null) // will be ignored
    }.size shouldBe 1

    testBookDao.search {
      field<String>("name").isNull()
      field<String>("price").isNotNull()
    }.size shouldBe 0
  }
})
