package puni.data.jpa

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.persistence.Entity
import puni.data.search.Searchable
import puni.data.search.impl.SearchableImpl

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
// to support Java 8
@SupportedOptions(EntityFieldProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class EntityFieldProcessor : AbstractProcessor() {

  companion object {
    const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
  }

  override fun getSupportedAnnotationTypes(): MutableSet<String> {
    return mutableSetOf(Entity::class.java.name)
  }

  override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
    roundEnv.getElementsAnnotatedWith(Entity::class.java).forEach {
      generate(element = it)
    }
    return false
  }

  private fun generate(element: Element) {
    val className = element.simpleName.toString()
    val pack = processingEnv.elementUtils.getPackageOf(element).toString()

    val fileName = "${className}Fields"
    val fileBuilder = FileSpec.builder(pack, fileName)
    val classBuilder = TypeSpec.objectBuilder(fileName)
    val searchableImpl = SearchableImpl::class.asClassName()

    element.enclosedElements.filter { it.kind == ElementKind.FIELD }.forEach { field ->
      val fieldName = field.simpleName.toString()
      // processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, "$fieldName ${field.asType().asTypeName()} ${String::class.asTypeName()}")
      classBuilder
        .addProperty(
          PropertySpec
            .builder(
              fieldName,
              Searchable::class.asClassName().parameterizedBy(
                element.asType().asTypeName(),
                if (field.asType().asTypeName().toString() == "java.lang.String") {
                  String::class.asTypeName()
                } else {
                  field.asType().asTypeName()
                }
              ),
              KModifier.PUBLIC
            )
            .initializer(
              CodeBlock.builder()
                .addStatement("""%T("$fieldName")""", searchableImpl)
                .build()
            )
            .build()
        )
    }

    val file = fileBuilder.addType(classBuilder.build()).build()
    val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
    file.writeTo(File(kaptKotlinGeneratedDir))
  }
}
