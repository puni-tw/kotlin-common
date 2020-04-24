package puni.zygarde

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.jetbrains.kotlin.config.JvmTarget
import org.springframework.core.io.ClassPathResource

class ZygardeProcessorTest : StringSpec({

  "should able to compile" {
    val result = KotlinCompilation().apply {
      sources = listOf(
        "puni/data/entity/User.kt",
        "puni/data/entity/Book.kt",
        "puni/data/entity/BookApiSpec.kt",
        "puni/data/entity/Author.kt",
        "puni/data/entity/AuthorApiSpec.kt",
        "puni/data/entity/BookTagsValueProvider.kt",
        "puni/data/model/SummaryModel.kt"
      ).map { ClassPathResource(it).file }.map { SourceFile.fromPath(it) }
      jvmTarget = JvmTarget.JVM_1_8.description
      // pass your own instance of an annotation processor
      annotationProcessors = listOf(ZygardeProcessor())

      // pass your own instance of a compiler plugin

      inheritClassPath = true
      messageOutputStream = System.out // see diagnostics in real time
    }.compile()

    result.exitCode shouldBe KotlinCompilation.ExitCode.OK
    result.generatedFiles.filter { it.name.endsWith(".kt") }.forEach {
      println("------------------")
      println(it.name)
      println(it.readText())
      println()
      println("------------------")
    }

    // val requestDtoClass = result.classLoader.loadClass("puni.data.entity.api.CreateBookBaseRequestDto")
    // JacksonCommon.setObjectMapper(
    //   jacksonObjectMapper().registerModule(JavaTimeModule())
    // )
    // val createRequestMap = mapOf(
    //   "name" to "Foo",
    //   "price" to 100,
    //   "priceD" to 100.0,
    //   "priceF" to 100.0f,
    //   "priceS" to 30000.toShort(),
    //   "releaseAt" to LocalDateTime.now()
    // )
    // val instance = createRequestMap.toJsonString().jsonStringToObject(requestDtoClass.kotlin)
    //
    // requestDtoClass.declaredFields
    //   .map {
    //     it.isAccessible = true
    //     it.name to it.get(instance)
    //   }
    //   .toMap().shouldContainAll(createRequestMap)
  }
})
