package puni.zygarde.generator

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.persistence.Id
import javax.persistence.IdClass
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import puni.exception.CommonErrorCode
import puni.extension.exception.errWhenNull
import puni.extension.kotlinpoet.kotlin
import puni.extension.kotlinpoet.kotlinTypeName
import puni.extension.kotlinpoet.name
import puni.extension.kotlinpoet.typeName

class ZygardeDaoGenerator(
  processingEnv: ProcessingEnvironment
) : AbstractZygardeGenerator(processingEnv) {

  fun generateBaseDaoForEntityElement(element: Element) {
    val daoName = "${element.name()}BaseDao"
    FileSpec.builder(packageName("data.dao"), daoName)
      .addType(
        TypeSpec.interfaceBuilder(daoName)
          .addSuperinterface(
            JpaRepository::class.generic(element.typeName(), element.findIdClass())
          )
          .addSuperinterface(
            JpaSpecificationExecutor::class.generic(element.typeName())
          )
          .build()
      )
      .build()
      .writeTo(fileTarget)
  }

  private fun Element.findIdClass(): TypeName {
    if (this.getAnnotation(IdClass::class.java) != null) {
      val idClass = this.getAnnotation(IdClass::class.java).value
      return idClass.asTypeName().kotlin()
    }
    return this.allFieldsIncludeSuper()
      .find { it.getAnnotation(Id::class.java) != null }
      .errWhenNull(CommonErrorCode.ERROR, "no id class found for entity ${this.simpleName}")
      .asType()
      .kotlinTypeName(false)
  }
}
