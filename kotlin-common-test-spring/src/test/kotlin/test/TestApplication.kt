package test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Import
import puni.test.spring.config.PuniSpringTestFeignConfig

@SpringBootApplication
@EnableFeignClients("test")
@Import(PuniSpringTestFeignConfig::class)
class TestApplication
