package test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import puni.test.spring.config.PuniSpringTestFeignConfig

/**
 * @author leo
 */
@SpringBootApplication
@ComponentScan("test", "puni.test")
@EnableFeignClients("puni")
@Import(PuniSpringTestFeignConfig::class)
class PuniSpringAuthApiTestApplication
