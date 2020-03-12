package puni.zygarde

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.kotlintest.matchers.maps.shouldContainAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.time.LocalDateTime
import puni.extension.jackson.JacksonCommon
import puni.extension.jackson.jsonStringToObject
import puni.extension.jackson.toJsonString

class EntityApiProcessorTest : StringSpec({

  val book = SourceFile.kotlin(
    "Book.kt",
    """
package puni.data.entity

import puni.zygarde.ApiProp
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
class Book(
  @ApiProp
  var name: String = "",
  @ApiProp
  var price: Int = 0,
  @ApiProp
  var priceD: Double? = null,
  @ApiProp
  var priceF: Float? = null,
  @ApiProp
  var priceS: Short? = null,
  @ManyToOne(targetEntity = Author::class)
  var author: Author = Author(),
  @ApiProp
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
      annotationProcessors = listOf(EntityApiProcessor())

      // pass your own instance of a compiler plugin

      inheritClassPath = true
      messageOutputStream = System.out // see diagnostics in real time
    }.compile()

    result.exitCode shouldBe KotlinCompilation.ExitCode.OK
    // result.generatedFiles.filter { it.name.endsWith(".kt") }.forEach {
    //   println(it.name)
    //   println(it.readText())
    // }

    val requestDtoClass = result.classLoader.loadClass("puni.data.entity.api.CreateBookBaseRequestDto")
    JacksonCommon.setObjectMapper(
      jacksonObjectMapper().registerModule(JavaTimeModule())
    )
    val createRequestMap = mapOf(
      "name" to "Foo",
      "price" to 100,
      "priceD" to 100.0,
      "priceF" to 100.0f,
      "priceS" to 30000.toShort(),
      "releaseAt" to LocalDateTime.now()
    )
    val instance = createRequestMap.toJsonString().jsonStringToObject(requestDtoClass.kotlin)

    requestDtoClass.declaredFields
      .map {
        it.isAccessible = true
        it.name to it.get(instance)
      }
      .toMap().shouldContainAll(createRequestMap)
  }
})
