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
import java.io.Serializable
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.persistence.Transient
import puni.data.search.EnhancedSearch
import puni.extension.kotlinpoet.fieldName
import puni.extension.kotlinpoet.isNullable
import puni.extension.kotlinpoet.kotlin
import puni.extension.kotlinpoet.name
import puni.extension.kotlinpoet.nullableTypeName
import puni.extension.kotlinpoet.typeName
import puni.zygarde.api.AdditionalDtoProps
import puni.zygarde.api.ApiProp
import puni.zygarde.api.DtoInherits
import puni.zygarde.api.SearchType
import puni.zygarde.api.value.NoOpValueProvider

class ZygardeApiPropGenerator(
  processingEnv: ProcessingEnvironment
) : AbstractZygardeGenerator(processingEnv) {

  data class DtoFieldDescriptionVo(
    val fieldName: String,
    val fieldType: TypeName,
    val dtoName: String,
    val comment: String,
    val dtoRef: String = "",
    val dtoRefCollection: Boolean = false,
    val valueProvider: TypeName? = null,
    val entityValueProvider: TypeName? = null,
    val generateToDtoExtension: Boolean = false,
    val generateApplyToEntityExtension: Boolean = false,
    val searchType: SearchType = SearchType.NONE,
    val searchForField: String? = null
  )

  val dtoPackageName = packageName("data.dto")

  fun generateDtoForEntityElement(element: Element) {
    val dtoInheritMap = (element.getAnnotation(DtoInherits::class.java)?.value ?: emptyArray())
      .map { it.dto to safeGetTypeFromAnnotation { it.inherit.asTypeName() } }
      .toMap()

    val dtoDescriptionsFromAdditionalDtoProps = (
      element.getAnnotation(AdditionalDtoProps::class.java)
        ?.let { additionalDtoProps ->
          additionalDtoProps.props.flatMap { additionalDtoProp ->
            additionalDtoProp.forDto.map {
              DtoFieldDescriptionVo(
                fieldName = additionalDtoProp.field,
                fieldType = safeGetTypeFromAnnotation { additionalDtoProp.fieldType.asTypeName() },
                dtoName = it,
                comment = additionalDtoProp.comment,
                valueProvider = safeGetTypeFromAnnotation { additionalDtoProp.valueProvider.asTypeName() }.validValueProvider(),
                entityValueProvider = safeGetTypeFromAnnotation { additionalDtoProp.entityValueProvider.asTypeName() }.validValueProvider(),
                generateToDtoExtension = true,
                generateApplyToEntityExtension = false
              )
            }
          }
        }
        ?: emptyList()
      )
    val dtoDescriptionsFromElementFields = element
      .allFieldsIncludeSuper()
      .flatMap { elem ->
        val isTransient = elem.getAnnotation(Transient::class.java) != null
        elem.getAnnotationsByType(ApiProp::class.java)
          .flatMap { apiProp ->
            listOf(
              apiProp.dto.map { dto ->
                toDtoFieldDescription(
                  elem = elem,
                  ref = dto.ref,
                  refClass = safeGetTypeFromAnnotation { dto.refClass.asTypeName() }.kotlin(false),
                  refCollection = dto.refCollection,
                  dtoName = dto.name,
                  comment = apiProp.comment,
                  valueProvider = safeGetTypeFromAnnotation { dto.valueProvider.asTypeName() }.kotlin(false).validValueProvider(),
                  entityValueProvider = safeGetTypeFromAnnotation { dto.entityValueProvider.asTypeName() }.kotlin(false).validValueProvider()
                ).copy(
                  generateToDtoExtension = dto.applyValueFromEntity,
                  generateApplyToEntityExtension = false
                )
              },
              apiProp.requestDto.map { dto ->
                toDtoFieldDescription(
                  elem = elem,
                  ref = dto.ref,
                  refClass = safeGetTypeFromAnnotation { dto.refClass.asTypeName() }.kotlin(false),
                  refCollection = dto.refCollection,
                  dtoName = dto.name,
                  comment = apiProp.comment,
                  valueProvider = safeGetTypeFromAnnotation { dto.valueProvider.asTypeName() }.kotlin(false).validValueProvider(),
                  forceNotNull = dto.notNullInReq
                ).copy(
                  generateToDtoExtension = false,
                  generateApplyToEntityExtension = !isTransient && dto.applyValueToEntity && dto.searchType == SearchType.NONE,
                  searchType = dto.searchType,
                  searchForField = dto.searchForField.takeIf { it.isNotEmpty() }
                )
              }
            ).flatten()
          }
          .toMutableList()
      }
    val allDescriptions = listOf(dtoDescriptionsFromAdditionalDtoProps, dtoDescriptionsFromElementFields).flatten()
    if (allDescriptions.isEmpty()) return

    val dtoExtensionName = "${element.name()}DtoExtensions"
    val fileBuilderForExtension = FileSpec.builder(dtoPackageName, dtoExtensionName)

    allDescriptions.groupBy { it.dtoName }
      .forEach { (dtoName, dtoFieldDescriptions) ->
        val dtoBuilder = TypeSpec.classBuilder(dtoName)
          .addModifiers(KModifier.DATA)
          .addAnnotation(ApiModel::class)
          .addSuperinterface(Serializable::class)

        dtoInheritMap.get(dtoName)?.let(dtoBuilder::superclass)

        val constructorBuilder = FunSpec.constructorBuilder()

        if (dtoFieldDescriptions.any { it.generateToDtoExtension }) {
          fileBuilderForExtension.addFunction(
            generateToDtoExtensionFunction(element, dtoName, dtoFieldDescriptions.filter { it.generateToDtoExtension })
          )
        }

        if (dtoFieldDescriptions.any { it.generateApplyToEntityExtension }) {
          fileBuilderForExtension.addFunction(
            generateApplyToEntityExtensionFunction(element, dtoName, dtoFieldDescriptions.filter { it.generateApplyToEntityExtension })
          )
        }

        val isSearchDto = dtoFieldDescriptions.any { it.searchType != SearchType.NONE }
        if (isSearchDto) {
          fileBuilderForExtension.addFunction(
            generateSearchExtensionFunction(element, dtoName, dtoFieldDescriptions.filter { it.searchType != SearchType.NONE })
          )
        }

        dtoFieldDescriptions.forEach { dto ->
          val fieldName = dto.fieldName
          val fieldType = dto.fieldType.let { if (isSearchDto) it.copy(nullable = true) else it }
          ParameterSpec
            .builder(fieldName, fieldType)
            .also {
              if (isSearchDto || fieldType.isNullable) {
                it.defaultValue("null")
              }
            }
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

  private fun toDtoFieldDescription(
    ref: String,
    refClass: TypeName,
    refCollection: Boolean,
    elem: Element,
    dtoName: String,
    comment: String,
    valueProvider: TypeName? = null,
    entityValueProvider: TypeName? = null,
    forceNotNull: Boolean = false
  ): DtoFieldDescriptionVo {
    val fieldName = elem.fieldName()
    val fieldType = when {
      ref.isNotEmpty() -> ClassName(dtoPackageName, ref).let {
        if (refCollection) {
          Collection::class.generic(it.kotlin(it.isNullable))
        } else {
          it
        }
      }
      refClass.toString() != "java.lang.Object" -> {
        if (refCollection) {
          Collection::class.generic(refClass.kotlin(refClass.isNullable))
        } else {
          refClass
        }
      }
      else -> elem.nullableTypeName()
    }
    return DtoFieldDescriptionVo(
      fieldName = fieldName,
      fieldType = fieldType.copy(nullable = !forceNotNull && elem.isNullable()),
      dtoName = dtoName,
      comment = comment,
      dtoRef = ref,
      dtoRefCollection = refCollection,
      valueProvider = valueProvider,
      entityValueProvider = entityValueProvider
    )
  }

  private fun TypeName?.validValueProvider(): TypeName? {
    return if (this != null && this.toString() != NoOpValueProvider::class.qualifiedName) {
      this
    } else {
      null
    }
  }

  private fun generateToDtoExtensionFunction(
    element: Element,
    dtoName: String,
    dtoFieldDescriptions: List<DtoFieldDescriptionVo>
  ): FunSpec {
    val codeBlockArgs = mutableListOf<Any>(ClassName(dtoPackageName, dtoName))
    val dtoFieldSetterStatements = dtoFieldDescriptions
      .map {
        val q = if (it.fieldType.isNullable) "?" else ""
        if (it.entityValueProvider != null) {
          codeBlockArgs.add(it.entityValueProvider)
          "  ${it.fieldName} = %T().getValue(this)"
        } else if (it.valueProvider != null) {
          codeBlockArgs.add(it.valueProvider)
          "  ${it.fieldName} = %T().getValue(this.${it.fieldName})"
        } else if (it.dtoRef.isNotEmpty()) {
          codeBlockArgs.add(MemberName(dtoPackageName, "to${it.dtoRef}"))
          if (it.dtoRefCollection) {
            "  ${it.fieldName} = this.${it.fieldName}$q.map{it.%M()}"
          } else {
            "  ${it.fieldName} = this.${it.fieldName}$q.%M()"
          }
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
        *codeBlockArgs.toTypedArray()
      )
      .build()
  }

  private fun generateApplyToEntityExtensionFunction(
    element: Element,
    dtoName: String,
    dtoFieldDescriptions: List<DtoFieldDescriptionVo>
  ): FunSpec {
    val functionBuilder = FunSpec.builder("applyFrom$dtoName")
      .addParameter("req", ClassName(dtoPackageName, dtoName))
      .receiver(element.typeName())

    dtoFieldDescriptions
      .forEach {
        if (it.valueProvider != null) {
          functionBuilder.addStatement(
            "this.${it.fieldName} = %T().getValue(req.${it.fieldName})",
            it.valueProvider
          )
        } else {
          functionBuilder.addStatement("this.${it.fieldName} = req.${it.fieldName}")
        }
      }

    return functionBuilder.build()
  }

  private fun generateSearchExtensionFunction(
    element: Element,
    dtoName: String,
    dtoFieldDescriptions: List<DtoFieldDescriptionVo>
  ): FunSpec {
    val dtoClass = ClassName(dtoPackageName, dtoName)
    val functionBuilder = FunSpec.builder("applyFrom$dtoName")
      .addParameter("req", dtoClass)
      .receiver(EnhancedSearch::class.generic(element.typeName()))

    dtoFieldDescriptions
      .forEach {
        val searchForField = it.searchForField ?: it.fieldName
        val fieldName = it.fieldName
        val fieldExtensionMember = MemberName("puni.zygarde.data.entity.search", searchForField)
        when (it.searchType) {
          SearchType.EQ -> functionBuilder.addStatement("%M() eq req.$fieldName", fieldExtensionMember)
          SearchType.NOT_EQ -> functionBuilder.addStatement("%M() ne req.$fieldName", fieldExtensionMember)
          SearchType.LT -> functionBuilder.addStatement("%M() lt req.$fieldName", fieldExtensionMember)
          SearchType.GT -> functionBuilder.addStatement("%M() gt req.$fieldName", fieldExtensionMember)
          SearchType.LTE -> functionBuilder.addStatement("%M() lte req.$fieldName", fieldExtensionMember)
          SearchType.GTE -> functionBuilder.addStatement("%M() gte req.$fieldName", fieldExtensionMember)
          SearchType.IN_LIST -> functionBuilder.addStatement("%M() inList req.$fieldName", fieldExtensionMember)
          SearchType.KEYWORD -> functionBuilder.addStatement("%M() keyword req.$fieldName", fieldExtensionMember)
          SearchType.STARTS_WITH -> functionBuilder.addStatement("%M() startsWith req.$fieldName", fieldExtensionMember)
          SearchType.ENDS_WITH -> functionBuilder.addStatement("%M() endsWith req.$fieldName", fieldExtensionMember)
          SearchType.CONTAINS -> functionBuilder.addStatement("%M() contains req.$fieldName", fieldExtensionMember)
          SearchType.DATE_RANGE -> functionBuilder.addStatement(
            "%M() %M req.$fieldName",
            fieldExtensionMember,
            MemberName("puni.data.search", "dateRange")
          )
          SearchType.DATE_TIME_RANGE -> functionBuilder.addStatement(
            "%M() %M req.$fieldName",
            fieldExtensionMember,
            MemberName("puni.data.search", "dateTimeRange")
          )
          else -> {
          }
        }
      }

    return functionBuilder.build()
  }
}
