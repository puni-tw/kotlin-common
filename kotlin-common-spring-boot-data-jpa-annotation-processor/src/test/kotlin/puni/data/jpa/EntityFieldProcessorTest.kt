package puni.data.jpa

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import puni.exception.CommonErrorCode
import puni.extension.exception.errWhenNull

class EntityFieldProcessorTest : StringSpec({

  val book = SourceFile.kotlin(
    "Book.kt",
    """
package puni.data.entity

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class Book(
  var name: String = "",
  var price: Int = 0,
  var priceD: Double? = null,
  var priceF: Float? = null,
  var priceS: Short? = null,
  @ManyToOne(targetEntity = Author::class)
  var author: Author = Author(),
  var releaseAt: LocalDateTime = LocalDateTime.now()
) : AutoIdEntity()
    
  """
  )

  val author = SourceFile.kotlin(
    "Author.kt",
    """
package puni.data.entity

import javax.persistence.Entity

@Entity
class Author(
  var name: String = "",
  var country: String = ""
) : AutoIdEntity()
  """
  )

  "should able to compile" {
    val result = KotlinCompilation().apply {
      sources = listOf(book, author)

      // pass your own instance of an annotation processor
      annotationProcessors = listOf(EntityFieldProcessor())

      // pass your own instance of a compiler plugin

      inheritClassPath = true
      messageOutputStream = System.out // see diagnostics in real time
    }.compile()

    result.exitCode shouldBe KotlinCompilation.ExitCode.OK
    result.generatedFiles.find { it.name == "BookFields.kt" }.errWhenNull(CommonErrorCode.ERROR)
      .readText()
      .also {
        it shouldContain """val name: Searchable<Book, String> = SearchableImpl("name")"""
        it shouldContain """val price: Searchable<Book, Int> = SearchableImpl("price")"""
        it shouldContain """val author: Searchable<Book, Author> = SearchableImpl("author")"""
        it shouldContain """val releaseAt: Searchable<Book, LocalDateTime> = SearchableImpl("releaseAt")"""
      }

    result.generatedFiles.find { it.name == "BookExtensions.kt" }.errWhenNull(CommonErrorCode.ERROR)
      .readText()
      .also {
        it shouldContain """fun EnhancedSearch<Book>.name(): ComparableConditionAction<Book, Book, String> =
    this.field(BookFields.name)"""
        it shouldContain """fun EnhancedSearch<Book>.price(): ComparableConditionAction<Book, Book, Int> =
    this.field(BookFields.price)"""
        it shouldContain """fun EnhancedSearch<Book>.author(): ConditionAction<Book, Book, Author> =
    this.field(BookFields.author)"""
        it shouldContain """fun ConditionAction<Book, Book, Author>.name(): ComparableConditionAction<Book, Author, String> =
    this.field(AuthorFields.name)"""
        it shouldContain """fun ConditionAction<Book, Book, Author>.country(): ComparableConditionAction<Book, Author, String> =
    this.field(AuthorFields.country)"""
        it shouldContain """fun EnhancedSearch<Book>.releaseAt(): ComparableConditionAction<Book, Book, LocalDateTime> =
    this.field(BookFields.releaseAt)"""
      }

    result.generatedFiles.find { it.name == "AuthorFields.kt" }.errWhenNull(CommonErrorCode.ERROR)
      .readText()
      .also {
        it shouldContain """val name: Searchable<Author, String> = SearchableImpl("name")"""
        it shouldContain """val country: Searchable<Author, String> = SearchableImpl("country")"""
      }

    result.generatedFiles.find { it.name == "AuthorExtensions.kt" }.errWhenNull(CommonErrorCode.ERROR)
      .readText()
      .also {
        it shouldContain """fun EnhancedSearch<Author>.name(): ComparableConditionAction<Author, Author, String> =
    this.field(AuthorFields.name)"""
        it shouldContain """fun EnhancedSearch<Author>.country(): ComparableConditionAction<Author, Author, String> =
    this.field(AuthorFields.country)"""
      }
  }
})
