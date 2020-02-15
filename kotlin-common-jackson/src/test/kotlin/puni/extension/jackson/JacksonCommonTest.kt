import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import puni.extension.jackson.JacksonCommon
import puni.extension.jackson.toJsonString

/**
 * @author leo
 */
class JacksonCommonTest : StringSpec({
  "should able to set objectMapper" {
    val date = Date.from(LocalDateTime.of(2020, 1, 1, 1, 1).atZone(ZoneId.of("UTC")).toInstant())
    val source = mapOf("foo" to date)
    val json = source.toJsonString()
    json shouldBe """{"foo":1577840460000}"""

    JacksonCommon.setObjectMapper(jacksonObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS))
    JacksonCommon.withObjectMapper { it.writeValueAsString(source) } shouldBe """{"foo":"2020-01-01T01:01:00.000+0000"}"""
  }
})
