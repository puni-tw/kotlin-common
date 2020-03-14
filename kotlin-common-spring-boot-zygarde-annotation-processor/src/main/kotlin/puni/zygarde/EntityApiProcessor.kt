package puni.zygarde

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
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
import puni.extension.kotlinpoet.fieldName
import puni.extension.kotlinpoet.isNullable
import puni.extension.kotlinpoet.nullableTypeName
import puni.extension.kotlinpoet.tryGetInitializeCodeBlock
import puni.extension.kotlinpoet.typeName
import puni.zygarde.api.ApiProp

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(EntityApiProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class EntityApiProcessor : AbstractProcessor() {

  companion object {
    const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
  }

  val fileTarget: String by lazy { "${processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]}" }

  override fun getSupportedAnnotationTypes(): MutableSet<String> {
    return mutableSetOf(Entity::class.java.name)
  }

  override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
    val elementsAnnotatedWithEntity = roundEnv.getElementsAnnotatedWith(Entity::class.java)
    elementsAnnotatedWithEntity.forEach {
      generateRequests(it)
    }
    return false
  }

  private fun generateRequests(element: Element) {
    val className = element.simpleName.toString()
    val pack = processingEnv.elementUtils.getPackageOf(element).toString() + ".api"
    val fieldsWihApiPropAnnotation = element.enclosedElements
      .filter { it.kind == ElementKind.FIELD }
      .mapNotNull { f -> f.getAnnotation(ApiProp::class.java)?.let { f to it } }

    val createFields = fieldsWihApiPropAnnotation.filter { it.second.create }
    val updateFields = fieldsWihApiPropAnnotation.filter { it.second.update }
    val createRequestName = "Create${className}BaseRequestDto"
    val updateRequestName = "Update${className}BaseRequestDto"
    generateApiRequest(pack, createRequestName, createFields)
    generateApiRequest(pack, updateRequestName, updateFields)

    generateCreateExtension(pack, element, createRequestName, createFields)
    generateUpdateExtension(pack, element, updateRequestName, updateFields)
  }

  private fun generateCreateExtension(
    pack: String,
    element: Element,
    createRequestName: String,
    createFields: List<Pair<Element, ApiProp>>
  ) {
    if (createFields.isEmpty()) return
    val className = element.simpleName.toString()
    val fileNameForExtension = "${className}RequestExtension"
    val fileBuilderForExtension = FileSpec.builder(pack, fileNameForExtension)
    val codeBlockBuilder = CodeBlock.builder()
      .addStatement(
        """return %T(
${createFields.map { it.first.fieldName() }.joinToString(",\r\n") { "  $it = this.$it" }}
)""".trimMargin(),
        element.asType()
      )
    val builder = FunSpec.builder("create$className")
      .receiver(ClassName(pack, createRequestName))
      .returns(element.typeName())
      .addCode(codeBlockBuilder.build())
    fileBuilderForExtension
      .addFunction(builder.build())
      .build()
      .writeTo(File(fileTarget))
  }

  private fun generateUpdateExtension(
    pack: String,
    element: Element,
    updateRequestName: String,
    updateFields: List<Pair<Element, ApiProp>>
  ) {
    if (updateFields.isEmpty()) return
    val className = element.simpleName.toString()
    val fileNameForExtension = "${className}UpdateExtension"
    val fileBuilderForExtension = FileSpec.builder(pack, fileNameForExtension)
    val codeBlockBuilder = CodeBlock.builder()
    codeBlockBuilder.addStatement("")
    updateFields.forEach {
      val filedName = it.first.fieldName()
      codeBlockBuilder.addStatement("this.$filedName = req.$filedName")
    }
    codeBlockBuilder.addStatement("return this")
    val builder = FunSpec.builder("applyUpdateRequest")
      .receiver(element.typeName())
      .returns(element.typeName())
      .addParameter("req", ClassName(pack, updateRequestName))
      .addCode(codeBlockBuilder.build())
    fileBuilderForExtension
      .addFunction(builder.build())
      .build()
      .writeTo(File(fileTarget))
  }

  private fun generateApiRequest(pack: String, fileName: String, fields: List<Pair<Element, ApiProp>>) {
    if (fields.isEmpty()) {
      return
    }
    FileSpec.builder(pack, fileName)
      .addType(
        TypeSpec.classBuilder(fileName)
          .addModifiers(KModifier.OPEN)
          .addAnnotation(ApiModel::class.java)
          .also { classBuilder ->
            fields
              .let(this@EntityApiProcessor::generateApiProps)
              .forEach { classBuilder.addProperty(it) }
          }
          .build()
      )
      .build()
      .writeTo(File(fileTarget))
  }

  private fun generateApiProps(fields: List<Pair<Element, ApiProp>>): List<PropertySpec> {
    return fields.map {
      val field = it.first
      val apiProp = it.second
      val propertyBuilder = PropertySpec
        .builder(field.fieldName(), field.nullableTypeName())
        .addAnnotation(
          AnnotationSpec.builder(ApiModelProperty::class)
            .addMember("notes=%S", apiProp.comment)
            .build()
        )
      if (field.isNullable()) {
        propertyBuilder.initializer("null")
      } else {
        val initializeCodeBlock = field.tryGetInitializeCodeBlock()
        if (initializeCodeBlock != null) {
          propertyBuilder.initializer(initializeCodeBlock)
        } else {
          propertyBuilder
            .mutable(true)
            .addModifiers(KModifier.LATEINIT)
        }
      }
      propertyBuilder.build()
    }
  }
}
