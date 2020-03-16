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
  testApi("com.github.tschuchortdev:kotlin-compile-testing:1.2.6")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.getByName("bootJar").enabled = false
tasks.getByName("jar").enabled = true
