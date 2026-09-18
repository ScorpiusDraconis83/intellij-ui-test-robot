plugins {
  kotlin("jvm") version "2.3.0" apply false
  id("org.jetbrains.intellij.platform") version "2.18.1" apply false
}

tasks.register<Copy>("copySampleProject") {
  description = "Copies sample-project out of the checkout, so a Remote Robot run cannot dirty it"
  from(layout.projectDirectory.dir("sample-project"))
  into(layout.buildDirectory.dir("sample-project"))
}
