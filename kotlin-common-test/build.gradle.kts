dependencies {
  implementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
  implementation("io.mockk:mockk:1.9.3")
}

tasks.getByName("printCoverage").enabled = false
