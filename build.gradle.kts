import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    jcenter()
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
  }
}

plugins {
  id("org.jlleitschuh.gradle.ktlint") version "9.1.1"
  kotlin("jvm") version "1.3.61"
  `maven-publish`
  jacoco
}

group = "puni"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
  mavenCentral()
  jcenter()
  maven("https://jitpack.io")
}

publishing {
  repositories {
    maven {
      name = "GitHubPackages"
      url = uri("https://maven.pkg.github.com/puni-tw/kotlin-common")
      credentials {
        username = System.getenv("PUNI_GH_PUBLISH_USER") ?: System.getenv("GITHUB_ACTOR")
        password = System.getenv("PUNI_GH_PUBLISH_TOKEN") ?: System.getenv("GITHUB_TOKEN")
      }
    }
  }
  publications {
    create<MavenPublication>("default") {
      from(components["java"])
    }
  }
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

ktlint {
  // debug.set(true)
  enableExperimentalRules.set(true)
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "1.8"
  }
}

tasks.jacocoTestReport {
  reports {
    html.isEnabled = true
    xml.isEnabled = true
  }
}
