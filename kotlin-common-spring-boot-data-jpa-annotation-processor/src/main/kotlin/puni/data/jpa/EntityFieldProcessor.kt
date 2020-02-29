package puni.data.jpa

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
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
import puni.data.search.ConditionAction
import puni.data.search.EnhancedSearch
import puni.data.search.Searchable
import puni.data.search.impl.SearchableImpl

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(EntityFieldProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class EntityFieldProcessor : AbstractProcessor() {

  companion object {
    const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
  }

  override fun getSupportedAnnotationTypes(): MutableSet<String> {
    return mutableSetOf(Entity::class.java.name)
  }

  override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
    val elementsAnnotatedWithEntity = roundEnv.getElementsAnnotatedWith(Entity::class.java)
    elementsAnnotatedWithEntity.forEach {
      generateFields(elementsAnnotatedWithEntity, it)
    }
    return false
  }

  private fun generateFields(allEntityElements: Set<Element>, element: Element) {
    val className = element.simpleName.toString()
    val pack = processingEnv.elementUtils.getPackageOf(element).toString() + ".search"

    val fileNameForFields = "${className}Fields"
    val fileNameForExtension = "${className}Extensions"
    val fileBuilderForExtension = FileSpec.builder(pack, fileNameForExtension)

    val classBuilder = TypeSpec.objectBuilder(fileNameForFields)
    val searchableImpl = SearchableImpl::class.asClassName()
    val entityTypeName = element.asType().asTypeName()

    element.enclosedElements.filter { it.kind == ElementKind.FIELD }.forEach { field ->
      val fieldName = field.simpleName.toString()
      // processingEnv.messager.printMessage(Diagnostic.Kind.WARNING, "$fieldName ${field.asType().asTypeName()} ${String::class.asTypeName()}")
      val fieldTypeName = if (field.asType().asTypeName().toString() == "java.lang.String") {
        String::class.asTypeName()
      } else {
        field.asType().asTypeName()
      }

      classBuilder
        .addProperty(
          PropertySpec
            .builder(
              fieldName,
              Searchable::class.asClassName().parameterizedBy(
                entityTypeName,
                fieldTypeName
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

      fileBuilderForExtension
        .addFunction(
          FunSpec.builder(fieldName)
            .receiver(
              EnhancedSearch::class.asClassName().parameterizedBy(entityTypeName)
            )
            .returns(
              ConditionAction::class.asClassName().parameterizedBy(
                entityTypeName,
                entityTypeName,
                fieldTypeName
              )
            )
            .addStatement("return this.field($fileNameForFields.$fieldName)")
            .build()
        )

      val relatedType = allEntityElements.find { it.asType() == field.asType() }
      if (relatedType != null) {
        val relateTypeName = relatedType.asType().asTypeName()
        relatedType.enclosedElements.filter { it.kind == ElementKind.FIELD }.forEach { fieldForRelativeType ->
          val fieldNameForRelativeType = fieldForRelativeType.simpleName.toString()
          val fieldTypeNameForRelativeType = if (fieldForRelativeType.asType().asTypeName().toString() == "java.lang.String") {
            String::class.asTypeName()
          } else {
            fieldForRelativeType.asType().asTypeName()
          }
          fileBuilderForExtension
            .addFunction(
              FunSpec.builder(fieldNameForRelativeType)
                .receiver(
                  ConditionAction::class.asClassName().parameterizedBy(
                    entityTypeName,
                    entityTypeName,
                    relateTypeName
                  )
                )
                .returns(
                  ConditionAction::class.asClassName().parameterizedBy(
                    entityTypeName,
                    relateTypeName,
                    fieldTypeNameForRelativeType
                  )
                )
                .addStatement("return this.field(${relatedType.simpleName}Fields.$fieldNameForRelativeType)")
                .build()
            )
        }
      }

      // processingEnv.messager.printMessage(
      //   Diagnostic.Kind.WARNING,
      //   "$fieldName ${allEntityElements.find { it.asType() == field.asType() }}"
      // )
    }

    fileBuilderForExtension
      .build()
      .writeTo(
        File("${processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]}")
      )

    FileSpec.builder(pack, fileNameForFields)
      .addType(classBuilder.build())
      .build()
      .writeTo(
        File("${processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]}")
      )
  }
}
