package puni.extension.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object JacksonCommon {
  private var objectMapper = jacksonObjectMapper()

  fun setObjectMapper(mapper: ObjectMapper) {
    this.objectMapper = mapper.registerKotlinModule()
  }

  fun objectMapper() = objectMapper

  fun <T : Any> withObjectMapper(block: (mapper: ObjectMapper) -> T) = block.invoke(objectMapper())
}
