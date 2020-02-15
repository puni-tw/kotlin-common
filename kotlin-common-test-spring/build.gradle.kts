apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

dependencies {
  api(project(":kotlin-common-test"))
  api("org.springframework.boot:spring-boot-starter-test")
  api("io.kotlintest:kotlintest-extensions-spring:3.4.2")
}

tasks.getByName("printCoverage").enabled = false
tasks.getByName("bootJar").enabled = false
tasks.getByName("jar").enabled = true
