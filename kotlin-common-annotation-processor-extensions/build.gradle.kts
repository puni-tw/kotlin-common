dependencies {
  implementation("com.squareup:kotlinpoet:1.5.0")
}

tasks.getByName("printCoverage").enabled = false
tasks.getByName("jacocoTestReport").enabled = false
