apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")
apply(plugin = "org.jetbrains.kotlin.plugin.spring")

dependencies {
  api(project(":kotlin-common"))
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  testApi(project(":kotlin-common-test-spring"))
  testImplementation("com.h2database:h2")
}

tasks.getByName("bootJar").enabled = false
tasks.getByName("jar").enabled = true
