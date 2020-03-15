package puni.zygarde

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import io.swagger.annotations.Api
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import puni.extension.kotlinpoet.fieldName
import puni.zygarde.option.OptionDto
import puni.zygarde.option.OptionEnum
import puni.zygarde.option.StaticOptionApi

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(StaticOptionApiProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class StaticOptionApiProcessor : AbstractProcessor() {

  companion object {
    const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
  }

  private val optionPackage = "puni.zygarde.api.option"
  private val optionDtoPackage = "$optionPackage.dto"
  private val optionDtoName = "StaticOptionDto"
  private val optionApiName = "StaticOptionApi"
  private val optionControllerPackage = "$optionPackage.impl"
  private val optionControllerName = "StaticOptionController"
  private val fileTarget: String by lazy { "${processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]}" }
  private val erasuredOptionEnum: TypeMirror by lazy {
    processingEnv.typeUtils.erasure(
      processingEnv.elementUtils.getTypeElement(OptionEnum::class.qualifiedName).asType()
    )
  }

  override fun getSupportedAnnotationTypes(): MutableSet<String> {
    return mutableSetOf(StaticOptionApi::class.java.name)
  }

  override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
    val elements = roundEnv.getElementsAnnotatedWith(StaticOptionApi::class.java)
      .filter { it.isOptionEnum() }
    if (elements.isNotEmpty()) {
      generateStaticOptionDto(elements)
      generateStaticOptionApi()
      generateStaticOptionController()
    }
    return false
  }

  private fun generateStaticOptionDto(elements: List<Element>) {
    val fileSpec = FileSpec.builder(optionDtoPackage, optionDtoName)
    val staticOptionDtoBuilder = TypeSpec.classBuilder(optionDtoName)
      .addModifiers(KModifier.DATA)
      .addAnnotation(ApiModel::class)
    val constructorBuilder = FunSpec.constructorBuilder()
    elements.forEach { elem ->
      val staticOptionApi = elem.getAnnotation(StaticOptionApi::class.java)
      val propType = Collection::class.asClassName().parameterizedBy(OptionDto::class.asClassName())
      ParameterSpec
        .builder(elem.fieldName(), propType)
        .defaultValue(
          CodeBlock.builder()
            .addStatement("%T.values().map{ it.toOptionDto() }", elem.asType())
            .build()
        )
        .build().also { constructorBuilder.addParameter(it) }
      PropertySpec
        .builder(elem.fieldName(), propType)
        .initializer(elem.fieldName())
        .addAnnotation(
          AnnotationSpec.builder(ApiModelProperty::class)
            .addMember("notes=%S", staticOptionApi.comment)
            .build()
        ).build().also { staticOptionDtoBuilder.addProperty(it) }
    }
    fileSpec
      .addType(
        staticOptionDtoBuilder.primaryConstructor(constructorBuilder.build()).build()
      )
      .build()
      .writeTo(File(fileTarget))
  }

  private fun generateStaticOptionApi() {
    val fileSpec = FileSpec.builder(optionPackage, optionApiName)
    val staticOptionApiBuilder = TypeSpec.interfaceBuilder(optionApiName)
      .addAnnotation(
        AnnotationSpec.builder(FeignClient::class)
          .addMember("name=%S", optionApiName)
          .build()
      )
      .addAnnotation(
        AnnotationSpec.builder(Api::class)
          .addMember("tags=[%S]", optionApiName)
          .build()
      )
      .addAnnotation(
        AnnotationSpec.builder(RequestMapping::class)
          .addMember(
            """value=["\_*puni.zygarde.api.static-option-api.path}"]"""
              .replace("_", "$")
              .replace("*", "{")
          )
          .build()
      )
      .addFunction(
        FunSpec.builder("getStaticOptions")
          .addModifiers(KModifier.ABSTRACT)
          .addAnnotation(GetMapping::class)
          .addAnnotation(
            AnnotationSpec.builder(ApiOperation::class)
              .addMember("value=%S", "Get All Static Options")
              .build()
          )
          .returns(
            ClassName(optionDtoPackage, optionDtoName)
          )
          .build()
      )
    fileSpec
      .addType(staticOptionApiBuilder.build())
      .build()
      .writeTo(File(fileTarget))
  }

  private fun generateStaticOptionController() {
    val fileSpec = FileSpec.builder(optionControllerPackage, optionControllerName)
    val staticOptionControllerBuilder = TypeSpec.classBuilder(optionControllerName)
      .addSuperinterface(
        ClassName(optionPackage, optionApiName)
      )
      .addAnnotation(RestController::class)
      .addFunction(
        FunSpec.builder("getStaticOptions")
          .addModifiers(KModifier.OVERRIDE)
          .returns(
            ClassName(optionDtoPackage, optionDtoName)
          )
          .addStatement("return %T()", ClassName(optionDtoPackage, optionDtoName))
          .build()
      )
    fileSpec
      .addType(staticOptionControllerBuilder.build())
      .build()
      .writeTo(File(fileTarget))
  }

  private fun Element.isOptionEnum(): Boolean {
    return processingEnv.typeUtils.isAssignable(this.asType(), erasuredOptionEnum)
  }
}
