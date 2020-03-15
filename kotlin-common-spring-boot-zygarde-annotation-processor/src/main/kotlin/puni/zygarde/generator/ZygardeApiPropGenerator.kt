package puni.zygarde.generator

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import puni.extension.kotlinpoet.fieldName
import puni.extension.kotlinpoet.kotlin
import puni.extension.kotlinpoet.name
import puni.extension.kotlinpoet.nullableTypeName
import puni.extension.kotlinpoet.typeName
import puni.zygarde.api.AdditionalDtoProps
import puni.zygarde.api.ApiProp
import puni.zygarde.api.NoOpValueProvider

class ZygardeApiPropGenerator(
  processingEnv: ProcessingEnvironment
) : AbstractZygardeGenerator(processingEnv) {

  data class DtoFieldDescriptionVo(
    val fieldName: String,
    val fieldType: TypeName,
    val name: String,
    val comment: String,
    val dtoRef: String = "",
    val valueProvider: TypeName? = null,
    val generateToDtoExtension: Boolean = true
  )

  val dtoPackageName = packageName("data.dto")

  fun generateDtoForEntityElement(element: Element) {
    val dtoDescriptionsFromAdditionalDtoProps = (
      element.getAnnotation(AdditionalDtoProps::class.java)
        ?.let { additionalDtoProps ->
          additionalDtoProps.props.flatMap { additionalDtoProp ->
            additionalDtoProp.forDto.map {
              DtoFieldDescriptionVo(
                fieldName = additionalDtoProp.field,
                fieldType = safeGetTypeFromAnnotation { additionalDtoProp.fieldType.asTypeName() },
                name = it,
                comment = additionalDtoProp.comment,
                valueProvider = safeGetTypeFromAnnotation { additionalDtoProp.valueProvider.asTypeName() }
              )
            }
          }
        }
        ?: emptyList()
      )
    val dtoDescriptionsFromElementFields = element
      .allFieldsIncludeSuper()
      .flatMap { elem ->
        elem.getAnnotationsByType(ApiProp::class.java)
          .flatMap { apiProp ->
            apiProp.dto.map { dto ->
              val fieldName = elem.fieldName()
              val refClass = safeGetTypeFromAnnotation { dto.refClass.asTypeName() }.kotlin(false)
              val valueProvider = safeGetTypeFromAnnotation { dto.valueProvider.asTypeName() }.kotlin(false)
              val fieldType = when {
                dto.ref.isNotEmpty() -> ClassName(dtoPackageName, dto.ref)
                refClass.toString() != "java.lang.Object" -> {
                  if (dto.refCollection) {
                    Collection::class.generic(refClass)
                  } else {
                    refClass
                  }
                }
                else -> elem.nullableTypeName()
              }
              DtoFieldDescriptionVo(
                fieldName = fieldName,
                fieldType = fieldType,
                name = dto.name,
                comment = apiProp.comment,
                dtoRef = dto.ref,
                valueProvider = if (valueProvider.toString() != NoOpValueProvider::class.qualifiedName) {
                  valueProvider
                } else {
                  null
                }
              )
            }
          }
          .toMutableList()
      }
    val allDescriptions = listOf(dtoDescriptionsFromAdditionalDtoProps, dtoDescriptionsFromElementFields).flatten()
    if (allDescriptions.isEmpty()) return

    val dtoExtensionName = "${element.name()}DtoExtensions"
    val fileBuilderForExtension = FileSpec.builder(dtoPackageName, dtoExtensionName)

    allDescriptions.groupBy { it.name }
      .forEach { (dtoGroup, dtoFieldDescriptions) ->
        val dtoName = "${element.name()}${dtoGroup}Dto"
        val dtoBuilder = TypeSpec.classBuilder(dtoName)
          .addModifiers(KModifier.DATA)
          .addAnnotation(ApiModel::class)
        val constructorBuilder = FunSpec.constructorBuilder()

        fileBuilderForExtension.addFunction(
          generateToDtoExtensionFunction(element, dtoName, dtoFieldDescriptions)
        )

        dtoFieldDescriptions.forEach { dto ->
          val fieldName = dto.fieldName
          val fieldType = dto.fieldType
          ParameterSpec
            .builder(fieldName, fieldType)
            .build().also { constructorBuilder.addParameter(it) }
          PropertySpec
            .builder(fieldName, fieldType)
            .initializer(fieldName)
            .addAnnotation(
              AnnotationSpec.builder(ApiModelProperty::class)
                .addMember("notes=%S", dto.comment)
                .build()
            ).build().also { dtoBuilder.addProperty(it) }
        }

        FileSpec.builder(dtoPackageName, dtoName)
          .addType(
            dtoBuilder.primaryConstructor(constructorBuilder.build()).build()
          )
          .build()
          .writeTo(fileTarget)
      }

    fileBuilderForExtension.build().writeTo(fileTarget)
  }

  private fun generateToDtoExtensionFunction(
    element: Element,
    dtoName: String,
    dtoFieldDescriptions: List<DtoFieldDescriptionVo>
  ): FunSpec {
    val dtoConstuctorArgs = mutableListOf<Any>(ClassName(dtoPackageName, dtoName))
    val dtoFieldSetterStatements = dtoFieldDescriptions
      .map {
        if (it.valueProvider != null) {
          dtoConstuctorArgs.add(it.valueProvider)
          "  ${it.fieldName} = %T().getValue(this)"
        } else if (it.dtoRef.isNotEmpty()) {
          dtoConstuctorArgs.add(MemberName(dtoPackageName, "to${it.dtoRef}"))
          "  ${it.fieldName} = this.${it.fieldName}.%M()"
        } else {
          "  ${it.fieldName} = this.${it.fieldName}"
        }
      }
    return FunSpec.builder("to$dtoName")
      .receiver(element.typeName())
      .addStatement(
        """return %T(
${dtoFieldSetterStatements.joinToString(",\r\n")}              
)""".trimMargin(),
        *dtoConstuctorArgs.toTypedArray()
      )
      .build()
  }
}
