package puni.extension.kotlinpoet

import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.type.DeclaredType
import kotlin.reflect.KClass

abstract class PuniCodeGenerator(val processingEnv: ProcessingEnvironment) {

  protected val fileTarget: File by lazy {
    File("${processingEnv.options[KaptOptions.KAPT_KOTLIN_GENERATED_OPTION_NAME]}")
  }

  protected fun Element.allFieldsIncludeSuper(): List<Element> {
    val superElements = processingEnv.typeUtils.directSupertypes(this.asType())
      .flatMap {
        if (it is DeclaredType) {
          it.asElement().allFieldsIncludeSuper()
        } else {
          emptyList()
        }
      }

    return listOf(superElements, this.enclosedElements)
      .flatten()
      .filter { it.kind == ElementKind.FIELD }
  }

  protected fun KClass<*>.generic(vararg typeName: TypeName): TypeName {
    return asClassName().parameterizedBy(
      *typeName
    )
  }
}
