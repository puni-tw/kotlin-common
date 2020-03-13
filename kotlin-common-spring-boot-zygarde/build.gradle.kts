apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")
apply(plugin = "org.jetbrains.kotlin.plugin.spring")

dependencies {
  api(project(":kotlin-common"))
  api(project(":kotlin-common-spring-boot-web"))
  api(project(":kotlin-common-spring-boot-data-jpa"))
  testApi(project(":kotlin-common-test"))
  testApi(project(":kotlin-common-test-spring"))
}

tasks.getByName("bootJar").enabled = false
tasks.getByName("jar").enabled = true
