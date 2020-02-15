import com.jfrog.bintray.gradle.BintrayExtension
import io.gitlab.arturbosch.detekt.detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    jcenter()
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
    maven("https://repo.spring.io/plugins-release")
  }
}

plugins {
  id("org.jlleitschuh.gradle.ktlint") version "9.1.1"
  id("org.jetbrains.dokka") version "0.10.0"
  id("io.gitlab.arturbosch.detekt") version "1.3.1"
  id("com.jfrog.bintray") version "1.8.4"
  id("de.jansauer.printcoverage") version "2.0.0"
  id("org.springframework.boot") version "2.2.2.RELEASE"
  id("io.spring.dependency-management") version "1.0.8.RELEASE"
  kotlin("jvm") version "1.3.61"
  kotlin("plugin.spring") version "1.3.61"
  `maven-publish`
  jacoco
}

allprojects {
  group = "puni"
  apply(plugin = "org.jlleitschuh.gradle.ktlint")

  repositories {
    mavenCentral()
    jcenter()
    maven("https://jitpack.io")
  }

  ktlint {
    enableExperimentalRules.set(true)
  }
}

subprojects {
  apply(plugin = "kotlin")
  apply(plugin = "org.jetbrains.kotlin.jvm")
  apply(plugin = "org.jetbrains.dokka")
  apply(plugin = "io.gitlab.arturbosch.detekt")
  apply(plugin = "org.gradle.jacoco")
  apply(plugin = "org.gradle.maven-publish")
  apply(plugin = "com.jfrog.bintray")
  apply(plugin = "de.jansauer.printcoverage")
  val subproject = this

  configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=strict")
      jvmTarget = "1.8"
    }
  }

  dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
    testImplementation("io.mockk:mockk:1.9.3")
  }

  task("housekeeping", Delete::class) {
    delete(file("out"))
  }

  tasks.getByName("clean").finalizedBy("housekeeping")
  tasks.getByName("test").finalizedBy("jacocoTestReport")
  tasks.getByName("jacocoTestReport").finalizedBy("printCoverage")

  tasks.withType<Test> {
    useJUnitPlatform()
  }

  tasks.withType<JacocoReport> {
    reports {
      html.isEnabled = true
      xml.isEnabled = true
      csv.isEnabled = false
    }
  }

  tasks.dokka {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
  }

  val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
  }

  val sourceJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Source"
    archiveClassifier.set("sources")
    from(subproject.sourceSets.getByName("main").allSource)
  }

  tasks.detekt {
    detekt {
      input = files("src/*/kotlin")
    }
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
        artifact(sourceJar)
        artifact(dokkaJar)
      }
    }
  }

  bintray {
    user = System.getenv("PUNI_TW_BINTRAY_USER")
    key = System.getenv("PUNI_TW_BINTRAY_API_KEY")
    publish = true
    setPublications("default")
    pkg(
      delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "maven"
        name = subproject.name
        userOrg = "puni"
        websiteUrl = "https://puni-tw.github.io/kotlin-common/doc/"
        githubRepo = "puni-tw/kotlin-common"
        vcsUrl = "https://github.com/puni-tw/kotlin-common"
        description = "Kotlin common utils and extensions"
        setLabels("kotlin")
        setLicenses("Apache-2.0")
        desc = description
      }
    )
  }
}

task("covAll", JacocoReport::class) {
  executionData(
    fileTree(rootDir.absolutePath).include("**/build/jacoco/*.exec")
  )
  sourceSets(
    *subprojects
      .map {
        it.sourceSets.getByName("main")
      }
      .toTypedArray()
  )
  reports {
    html.isEnabled = true
    xml.isEnabled = true
  }

  dependsOn(
    *subprojects.map { it.tasks.getByName("test") }.toTypedArray()
  )
}

tasks.getByName("bintrayUpload").enabled = false
tasks.getByName("printCoverage").enabled = false

tasks.dokka {
  outputFormat = "html"
  outputDirectory = "$buildDir/javadoc"
  subProjects = subprojects.map { it.name }
  configuration {
    moduleName = "doc"
  }
}

task("collectJacocoSourcePath", Exec::class) {
  val paths = subprojects
    .flatMap { it.sourceSets.getByName("main").allJava.srcDirs }
    .joinToString(" ")
  commandLine = listOf("echo", paths)
}
