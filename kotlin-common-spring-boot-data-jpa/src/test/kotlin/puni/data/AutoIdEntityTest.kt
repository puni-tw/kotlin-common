package puni.data

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.util.Objects
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import puni.data.dao.AutoIdDao
import puni.data.entity.Bar
import puni.data.entity.Book

class Foo(val id: Long, val name: String = "")

interface TestBookDao : AutoIdDao<Book>
interface TestBarDao : AutoIdDao<Bar>

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = [PuniSpringBootDataJpaTestApplication::class])
@ActiveProfiles("test")
class AutoIdEntityTest(
  val testBookDao: TestBookDao,
  val testBarDao: TestBarDao
) : StringSpec({
  "should able to save and increase id" {
    val book = testBookDao.save(Book()).also {
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
    testBookDao.saveAll(
      setOf(Book(), Book())
    )
  }
})
