package puni.data.jpa

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
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
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Transient
import puni.data.search.ComparableConditionAction
import puni.data.search.ConditionAction
import puni.data.search.EnhancedSearch
import puni.data.search.Searchable
import puni.data.search.impl.SearchableImpl
import puni.extension.kotlinpoet.KaptOptions
import puni.extension.kotlinpoet.fieldName
import puni.extension.kotlinpoet.typeName

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(KaptOptions.KAPT_KOTLIN_GENERATED_OPTION_NAME)
open class EntityFieldProcessor : AbstractProcessor() {

  val erasuredComparable: TypeMirror by lazy {
    val comparableType = processingEnv.elementUtils.getTypeElement("java.lang.Comparable").asType()
    processingEnv.typeUtils.erasure(comparableType)
  }

  val searchableImpl: ClassName by lazy { SearchableImpl::class.asClassName() }

  val fileTarget: String by lazy { "${processingEnv.options[KaptOptions.KAPT_KOTLIN_GENERATED_OPTION_NAME]}" }

  override fun getSupportedAnnotationTypes(): MutableSet<String> {
    return mutableSetOf(Entity::class.java.name)
  }

  override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
    val elementsAnnotatedWithEntity = roundEnv.getElementsAnnotatedWith(Entity::class.java)
    elementsAnnotatedWithEntity.forEach {
      generateFields(it)
      generateExtensionFunctions(elementsAnnotatedWithEntity, it)
    }
    return false
  }

  private fun generateFields(element: Element) {
    val className = element.simpleName.toString()
    val pack = getTargetPackage(element)
    val fileNameForFields = "${className}Fields"

    val classBuilder = TypeSpec.objectBuilder(fileNameForFields)
    val entityTypeName = element.asType().asTypeName()

    element.allFields().forEach { field ->
      classBuilder.addProperty(
        field.buildSearchableProperty(entityTypeName)
      )
    }

    FileSpec.builder(pack, fileNameForFields)
      .addType(classBuilder.build())
      .build()
      .writeTo(File(fileTarget))
  }

  private fun generateExtensionFunctions(allEntityElements: Set<Element>, element: Element) {
    val className = element.simpleName.toString()
    val pack = getTargetPackage(element)
    val fileNameForExtension = "${className}Extensions"
    val fileBuilderForExtension = FileSpec.builder(pack, fileNameForExtension)
    val rootEntityType = element.typeName()

    element.allFields()
      .forEach { field ->
        val fieldName = field.simpleName.toString()

        fileBuilderForExtension
          .addFunction(
            FunSpec.builder(fieldName)
              .receiver(EnhancedSearch::class.asClassName().parameterizedBy(rootEntityType))
              .returns(field.toConditionAction(rootEntityType, rootEntityType))
              .addStatement("return this.field(${className}Fields.$fieldName)")
              .build()
          )

        val relatedTypeElement = allEntityElements.find { it.asType() == field.asType() }
        relatedTypeElement?.allFields()?.forEach { fieldForRelativeType ->
          fileBuilderForExtension
            .addFunction(
              fieldForRelativeType.buildRelateTypeConditionAction(element, relatedTypeElement)
            )
        }
      }

    fileBuilderForExtension.build().writeTo(File(fileTarget))
  }

  open fun getTargetPackage(element: Element) = processingEnv.elementUtils.getPackageOf(element).toString() + ".search"

  private fun Element.buildSearchableProperty(
    entityTypeName: TypeName
  ): PropertySpec {
    return PropertySpec
      .builder(
        fieldName(),
        Searchable::class.asClassName().parameterizedBy(
          entityTypeName,
          typeName()
        ),
        KModifier.PUBLIC
      )
      .initializer(
        CodeBlock.builder()
          .addStatement("""%T("${fieldName()}")""", searchableImpl)
          .build()
      )
      .build()
  }

  private fun Element.buildRelateTypeConditionAction(rootEntityElement: Element, currentEntityElement: Element): FunSpec {
    return FunSpec.builder(fieldName())
      .receiver(
        ConditionAction::class.asClassName().parameterizedBy(
          rootEntityElement.typeName(),
          rootEntityElement.typeName(),
          currentEntityElement.typeName()
        )
      )
      .returns(
        toConditionAction(rootEntityElement.typeName(), currentEntityElement.typeName())
      )
      .addStatement("return this.field(${currentEntityElement.simpleName}Fields.${fieldName()})")
      .build()
  }

  private fun Element.isComparable(): Boolean {
    return processingEnv.typeUtils.isAssignable(this.asType(), erasuredComparable)
  }

  private fun Element.toConditionAction(rootEntityTypeName: TypeName, currentEntityTypeName: TypeName): TypeName {
    return if (isComparable()) {
      ComparableConditionAction::class.asClassName().parameterizedBy(
        rootEntityTypeName,
        currentEntityTypeName,
        typeName()
      )
    } else {
      ConditionAction::class.asClassName().parameterizedBy(
        rootEntityTypeName,
        currentEntityTypeName,
        typeName()
      )
    }
  }

  private fun Element.allFields(): List<Element> {
    val superElements = processingEnv.typeUtils.directSupertypes(this.asType())
      .flatMap {
        if (it is DeclaredType) {
          it.asElement().enclosedElements
        } else {
          emptyList()
        }
      }

    return listOf(superElements, this.enclosedElements)
      .flatten()
      .filter { it.kind == ElementKind.FIELD }
      .filter {
        it.getAnnotation(Transient::class.java) == null
      }
      .filter {
        // TODO OneToMany and ManyToMany are not supported right now
        it.getAnnotation(OneToMany::class.java) == null && it.getAnnotation(ManyToMany::class.java) == null
      }
  }
}
