apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

dependencies {
  compileOnly(project(":kotlin-common-spring-boot-data-jpa"))
  implementation(project(":kotlin-common-annotation-processor-extensions"))
  implementation("jakarta.persistence:jakarta.persistence-api:2.2.3")
  implementation("com.squareup:kotlinpoet:1.5.0")
  implementation("com.google.auto.service:auto-service:1.0-rc6")
  kapt("com.google.auto.service:auto-service:1.0-rc6")

  testApi(project(":kotlin-common-spring-boot-data-jpa"))
  testApi(project(":kotlin-common-test"))
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.2.6")
}

tasks.getByName("bootJar").enabled = false
tasks.getByName("jar").enabled = true
