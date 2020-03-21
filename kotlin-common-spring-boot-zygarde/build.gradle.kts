apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")
apply(plugin = "org.jetbrains.kotlin.plugin.spring")

dependencies {
  api(project(":kotlin-common"))
  api(project(":kotlin-common-spring-boot-web"))
  api(project(":kotlin-common-spring-boot-data-jpa"))
  api("org.springframework.cloud:spring-cloud-starter-openfeign")
  api("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("io.swagger:swagger-annotations:1.6.0")
  testApi(project(":kotlin-common-test"))
  testApi(project(":kotlin-common-test-spring"))
  kapt("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.getByName("bootJar").enabled = false
tasks.getByName("jar").enabled = true
