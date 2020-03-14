apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

dependencies {
  compileOnly(project(":kotlin-common-spring-boot-data-jpa"))
  compileOnly(project(":kotlin-common-spring-boot-web"))
  compileOnly(project(":kotlin-common-spring-boot-zygarde"))
  implementation(project(":kotlin-common-annotation-processor-extensions"))
  implementation("io.swagger:swagger-annotations:1.6.0")
  implementation("jakarta.persistence:jakarta.persistence-api:2.2.3")
  implementation("com.squareup:kotlinpoet:1.5.0")
  implementation("com.google.auto.service:auto-service:1.0-rc6")
  api(project(":kotlin-common-spring-boot-data-jpa-annotation-processor"))
  kapt("com.google.auto.service:auto-service:1.0-rc6")

  testApi(project(":kotlin-common-spring-boot-data-jpa"))
  testApi(project(":kotlin-common-spring-boot-web"))
  testApi(project(":kotlin-common-jackson"))
  testApi(project(":kotlin-common-spring-boot-zygarde"))
  testApi(project(":kotlin-common-test"))
  testImplementation("org.springframework.boot:spring-boot-starter-json")
  testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.2.6")
}

tasks.getByName("bootJar").enabled = false
tasks.getByName("jar").enabled = true
