package puni.test

import com.google.common.collect.Sets
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.RequestMethod
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * @author leo
 */
@SpringBootApplication
@EnableSwagger2
@ComponentScan("puni.spring.web.controller")
class TestApplication {

  @Bean
  fun docket(): Docket {
    return Docket(DocumentationType.SWAGGER_2)
      .globalResponseMessage(RequestMethod.GET, mutableListOf())
      .globalResponseMessage(RequestMethod.POST, mutableListOf())
      .globalResponseMessage(RequestMethod.PUT, mutableListOf())
      .globalResponseMessage(RequestMethod.DELETE, mutableListOf())
      .consumes(Sets.newHashSet("application/json"))
      .produces(Sets.newHashSet("application/json"))
      .select()
      .apis(RequestHandlerSelectors.basePackage("puni"))
      .paths(PathSelectors.ant("/api/**"))
      .build()
      .host("http://example.com")
      .apiInfo(
        ApiInfo(
          "Api",
          "",
          "1.0",
          "",
          Contact("", "", ""),
          "",
          "",
          mutableListOf()
        )
      )
  }
}
