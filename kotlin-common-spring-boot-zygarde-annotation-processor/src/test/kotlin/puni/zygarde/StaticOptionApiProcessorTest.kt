package puni.zygarde

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.springframework.core.io.ClassPathResource

class StaticOptionApiProcessorTest : StringSpec({

  "should able to compile" {
    val result = KotlinCompilation().apply {
      sources = listOf(
        "puni/options/FooType.kt",
        "puni/options/BarType.kt"
      ).map { SourceFile.fromPath(ClassPathResource(it).file) }

      // pass your own instance of an annotation processor
      annotationProcessors = listOf(StaticOptionApiProcessor())

      // pass your own instance of a compiler plugin

      inheritClassPath = true
      messageOutputStream = System.out // see diagnostics in real time
    }.compile()

    result.exitCode shouldBe KotlinCompilation.ExitCode.OK
    result.generatedFiles.filter { it.name.endsWith(".kt") }.forEach {
      println(it.name)
      println(it.readText())
    }
  }
})
