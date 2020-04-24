package puni.zygarde

import com.google.auto.service.AutoService
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.persistence.Entity
import puni.data.jpa.EntityFieldProcessor
import puni.extension.kotlinpoet.KaptOptions
import puni.zygarde.api.ZygardeApi
import puni.zygarde.api.ZygardeModel
import puni.zygarde.generator.ZygardeApiGenerator
import puni.zygarde.generator.ZygardeApiPropGenerator
import puni.zygarde.generator.ZygardeDaoGenerator

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(KaptOptions.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class ZygardeProcessor : EntityFieldProcessor() {

  override fun getSupportedAnnotationTypes(): MutableSet<String> {
    return mutableSetOf(ZygardeModel::class.java.name, ZygardeApi::class.java.name)
  }

  override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
    super.process(annotations, roundEnv)

    val elementsAnnotatedWithEntity = roundEnv.getElementsAnnotatedWith(Entity::class.java)
    elementsAnnotatedWithEntity.forEach {
      ZygardeDaoGenerator(processingEnv).generateBaseDaoForEntityElement(it)
    }

    val elementsAnnotatedWithZygardeModel = roundEnv.getElementsAnnotatedWith(ZygardeModel::class.java)
    elementsAnnotatedWithZygardeModel.forEach {
      ZygardeApiPropGenerator(processingEnv).generateDtoForEntityElement(it)
    }

    val elementsAnnotatedWithZygardApi = roundEnv.getElementsAnnotatedWith(ZygardeApi::class.java)
    ZygardeApiGenerator(processingEnv).generateApi(elementsAnnotatedWithZygardApi)
    return false
  }

  override fun getTargetPackage(element: Element): String = "puni.zygarde.data.entity.search"
}
