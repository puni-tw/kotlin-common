dependencies {
  implementation("com.squareup:kotlinpoet:1.5.0")
}

tasks.filter { it.group == "publishing" }.forEach {
  it.enabled = false
}
tasks.getByName("printCoverage").enabled = false
tasks.getByName("jacocoTestReport").enabled = false
