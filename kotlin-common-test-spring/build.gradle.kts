apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")
apply(plugin = "org.jetbrains.kotlin.plugin.spring")

dependencies {
  api(project(":kotlin-common-test"))
  api(project(":kotlin-common-jackson"))
  api("org.springframework.boot:spring-boot-starter-test")
  api("io.kotlintest:kotlintest-extensions-spring:3.4.2")
  api("org.springframework.cloud:spring-cloud-starter-openfeign")
  api("org.springframework.boot:spring-boot-starter-web")
  api("com.ninja-squad:springmockk:2.0.0")
  api(project(":kotlin-common-spring-boot-web"))
}

tasks.getByName("printCoverage").enabled = false
tasks.getByName("bootJar").enabled = false
tasks.getByName("jar").enabled = true
