apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")
apply(plugin = "org.jetbrains.kotlin.plugin.spring")

dependencies {
  api(project(":kotlin-common"))
  api(project(":kotlin-common-log"))
  api(project(":kotlin-common-jackson"))
  implementation("io.swagger:swagger-annotations:1.6.0")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  testApi(project(":kotlin-common-test-spring"))
}

tasks.getByName("bootJar").enabled = false
tasks.getByName("jar").enabled = true
