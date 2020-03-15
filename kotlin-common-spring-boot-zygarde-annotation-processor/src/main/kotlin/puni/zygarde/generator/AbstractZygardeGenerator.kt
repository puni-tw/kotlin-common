package puni.zygarde.generator

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.type.MirroredTypeException
import puni.extension.kotlinpoet.PuniCodeGenerator

abstract class AbstractZygardeGenerator(
  processingEnv: ProcessingEnvironment
) : PuniCodeGenerator(processingEnv) {
  fun packageName(pack: String) = "puni.zygarde.$pack"

  fun safeGetTypeFromAnnotation(block: () -> TypeName): TypeName {
    try {
      return block.invoke()
    } catch (e: MirroredTypeException) {
      return e.typeMirror.asTypeName()
    }
  }
}
