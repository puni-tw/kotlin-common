package puni.zygarde

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.kotlintest.matchers.maps.shouldContainAll
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.time.LocalDateTime
import org.springframework.core.io.ClassPathResource
import puni.data.jpa.EntityFieldProcessor
import puni.extension.jackson.JacksonCommon
import puni.extension.jackson.jsonStringToObject
import puni.extension.jackson.toJsonString

class EntityApiProcessorTest : StringSpec({

  "should able to compile" {
    val book = SourceFile.fromPath(
      ClassPathResource("puni/data/entity/Book.kt").file
    )
    val author = SourceFile.fromPath(
      ClassPathResource("puni/data/entity/Author.kt").file
    )

    val result = KotlinCompilation().apply {
      sources = listOf(book, author)

      // pass your own instance of an annotation processor
      annotationProcessors = listOf(EntityFieldProcessor(), EntityApiProcessor())

      // pass your own instance of a compiler plugin

      inheritClassPath = true
      messageOutputStream = System.out // see diagnostics in real time
    }.compile()

    result.exitCode shouldBe KotlinCompilation.ExitCode.OK
    result.generatedFiles.filter { it.name.endsWith(".kt") }.forEach {
      println(it.name)
      println(it.readText())
    }

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
