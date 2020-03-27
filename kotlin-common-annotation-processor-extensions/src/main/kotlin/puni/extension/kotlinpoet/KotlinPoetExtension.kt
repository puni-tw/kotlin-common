package puni.extension.kotlinpoet

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror
import org.jetbrains.annotations.Nullable

fun Element.name() = simpleName.toString()

fun Element.fieldName() = simpleName.toString().decapitalize()

fun Element.isNullable() = this.getAnnotation(Nullable::class.java) != null

fun Element.typeName(): TypeName {
  return typeName(canBeNullable = false)
}

fun Element.nullableTypeName(): TypeName {
  return typeName(canBeNullable = true)
}

fun Element.isPrimitive(): Boolean {
  return asType().kind.isPrimitive
}

fun Element.tryGetInitializeCodeBlock(): CodeBlock? {
  val builder = CodeBlock.Builder()
  when (asType().asTypeName().toString()) {
    "java.lang.String" -> builder.addStatement("%S", "")
    "java.lang.Integer" -> builder.addStatement("%L", "0")
    "kotlin.Int" -> builder.addStatement("%L", "0")
    "java.lang.Long" -> builder.addStatement("%L", "0L")
    "java.lang.Double" -> builder.addStatement("%L", "0.0")
    "java.lang.Float" -> builder.addStatement("%L", "0.0")
    "java.lang.Short" -> builder.addStatement("%L", "0")
    "java.lang.Boolean" -> builder.addStatement("%L", "true")
    else -> return null
  }
  return builder.build()
}

fun TypeName.kotlin(canBeNullable: Boolean = true): TypeName {
  return when (toString()) {
    "java.lang.String" -> String::class.asTypeName()
    "java.lang.Integer" -> Int::class.asTypeName()
    "java.lang.Long" -> Long::class.asTypeName()
    "java.lang.Double" -> Double::class.asTypeName()
    "java.lang.Float" -> Float::class.asTypeName()
    "java.lang.Short" -> Short::class.asTypeName()
    "java.lang.Boolean" -> Boolean::class.asTypeName()
    else -> this
  }.copy(nullable = canBeNullable)
}

fun TypeMirror.kotlinTypeName(canBeNullable: Boolean = true): TypeName {
  return when (asTypeName().toString()) {
    "java.lang.String" -> String::class.asTypeName()
    "java.lang.Integer" -> Int::class.asTypeName()
    "java.lang.Long" -> Long::class.asTypeName()
    "java.lang.Double" -> Double::class.asTypeName()
    "java.lang.Float" -> Float::class.asTypeName()
    "java.lang.Short" -> Short::class.asTypeName()
    "java.lang.Boolean" -> Boolean::class.asTypeName()
    else -> asTypeName()
  }.copy(nullable = canBeNullable)
}

private fun Element.typeName(canBeNullable: Boolean = true): TypeName {
  return this.asType().kotlinTypeName(canBeNullable && isNullable())
}
