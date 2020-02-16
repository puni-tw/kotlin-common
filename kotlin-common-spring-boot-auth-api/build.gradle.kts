apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")
apply(plugin = "org.jetbrains.kotlin.plugin.spring")

dependencies {
  api(project(":kotlin-common"))
  api(project(":kotlin-common-log"))
  api(project(":kotlin-common-jackson"))
  api(project(":kotlin-common-spring-boot-web"))
  api("io.jsonwebtoken:jjwt-api:0.11.0")
  api("io.jsonwebtoken:jjwt-impl:0.11.0")
  api("io.jsonwebtoken:jjwt-jackson:0.11.0")
  implementation("io.swagger:swagger-annotations:1.6.0")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
  testApi(project(":kotlin-common-test-spring"))
}

tasks.getByName("bootJar").enabled = false
tasks.getByName("jar").enabled = true
