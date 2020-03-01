dependencies {
  compileOnly(project(":kotlin-common-spring-boot-data-jpa"))
  implementation("jakarta.persistence:jakarta.persistence-api:2.2.3")
  implementation("com.squareup:kotlinpoet:1.5.0")
  implementation("com.google.auto.service:auto-service:1.0-rc6")
  kapt("com.google.auto.service:auto-service:1.0-rc6")
}

tasks.getByName("printCoverage").enabled = false
