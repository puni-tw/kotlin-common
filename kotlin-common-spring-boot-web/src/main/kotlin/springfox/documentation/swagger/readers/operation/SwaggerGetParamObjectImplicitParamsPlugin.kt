package springfox.documentation.swagger.readers.operation

import com.google.common.base.MoreObjects
import com.google.common.base.Strings.emptyToNull
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import puni.extension.exception.nullWhenError
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.schema.ModelRef
import springfox.documentation.schema.Types.isBaseType
import springfox.documentation.service.AllowableValues
import springfox.documentation.service.Parameter
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.OperationBuilderPlugin
import springfox.documentation.spi.service.contexts.OperationContext
import springfox.documentation.spring.web.DescriptionResolver
import springfox.documentation.swagger.common.SwaggerPluginSupport
import springfox.documentation.swagger.common.SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER
import springfox.documentation.swagger.readers.parameter.Examples.examples
import springfox.documentation.swagger.schema.ApiModelProperties.allowableValueFromString

/**
 * @author leo
 */
@Component
class SwaggerGetParamObjectImplicitParamsPlugin(val descriptionResolver: DescriptionResolver) : OperationBuilderPlugin {

  internal fun implicitParameter(descriptions: DescriptionResolver, param: ApiImplicitParam): Parameter {
    val modelRef = maybeGetModelRef(param)
    return ParameterBuilder()
      .name(param.name)
      .description(descriptions.resolve(param.value))
      .defaultValue(param.defaultValue)
      .required(param.required)
      .allowMultiple(param.allowMultiple)
      .modelRef(modelRef)
      .allowableValues(allowableValueFromString(param.allowableValues))
      .parameterType(emptyToNull(param.paramType))
      .parameterAccess(param.access)
      .order(SWAGGER_PLUGIN_ORDER)
      .scalarExample(param.example)
      .complexExamples(examples(param.examples))
      .build()
  }

  private fun maybeGetModelRef(param: ApiImplicitParam): ModelRef {
    val dataType = MoreObjects.firstNonNull(emptyToNull(param.dataType), "string")
    var allowableValues: AllowableValues? = null
    if (isBaseType(dataType)) {
      allowableValues = allowableValueFromString(param.allowableValues)
    }
    return if (param.allowMultiple) {
      ModelRef("", ModelRef(dataType, allowableValues))
    } else ModelRef(dataType, allowableValues)
  }

  override fun apply(context: OperationContext) {
    context.parameters.forEach {
      val annotation = nullWhenError { AnnotationUtils.findAnnotation(Class.forName(it.parameterType.toString()), ApiImplicitParams::class.java) }
      if (annotation != null) {
        context.operationBuilder().parameters(
          annotation.value.map {
            implicitParameter(descriptionResolver, it)
          }
        )
      }
    }
  }

  override fun supports(delimiter: DocumentationType): Boolean {
    return SwaggerPluginSupport.pluginDoesApply(delimiter)
  }
}
