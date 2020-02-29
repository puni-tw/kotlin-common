apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")
apply(plugin = "org.jetbrains.kotlin.plugin.spring")

dependencies {
  implementation(project(":kotlin-common-spring-boot-data-jpa"))
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  kapt(project(":kotlin-common-spring-boot-data-jpa-annotation-processor"))
  testApi(project(":kotlin-common-test-spring"))
  testImplementation("com.h2database:h2")
}
