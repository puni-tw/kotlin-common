apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

dependencies {
  api("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.2")
  api("org.springframework.boot:spring-boot-starter-json")
}

tasks.getByName("bootJar").enabled = false
tasks.getByName("jar").enabled = true
