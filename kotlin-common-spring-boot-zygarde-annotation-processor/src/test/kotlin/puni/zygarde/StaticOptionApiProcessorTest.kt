package puni.zygarde

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class StaticOptionApiProcessorTest : StringSpec({

  val fooType = SourceFile.kotlin(
    "FooType.kt",
    """
package puni.options

import puni.zygarde.option.StaticOptionApi
import puni.zygarde.option.OptionEnum

@StaticOptionApi(comment = "foo bar")
enum class FooType(
  override val label: String
): OptionEnum {
  FOO("foo"),
  BAR("bar")
}
    
  """
  )

  val barType = SourceFile.kotlin(
    "BarType.kt",
    """
package puni.options

import puni.zygarde.option.StaticOptionApi
import puni.zygarde.option.OptionEnum

@StaticOptionApi(comment = "bar bar")
enum class BarType(
  override val label: String
): OptionEnum {
  LOL("lol"),
  QAQ("qaq")
}
    
  """
  )

  "should able to compile" {
    val result = KotlinCompilation().apply {
      sources = listOf(fooType, barType)

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
