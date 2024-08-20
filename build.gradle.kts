import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

val ktor: String by project
val kotlin: String by project
val logback: String by project
val postgres: String by project
val commonsCodec: String by project
val exposed: String by project

plugins {
  kotlin("jvm") version "1.9.22"
  id("io.ktor.plugin") version "2.3.7"
  id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
  id("org.jlleitschuh.gradle.ktlint") version "12.1.0"
}

subprojects {
  apply(plugin = "org.jlleitschuh.gradle.ktlint")

  configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    android.set(true)
    verbose.set(true)
    reporters {
      reporter(ReporterType.PLAIN)
      reporter(ReporterType.CHECKSTYLE)
      reporter(ReporterType.JSON)
    }
  }
}

tasks["check"].finalizedBy(tasks["ktlintCheck"])

group = "com.kenkoro.taurus.api.client"
version = "0.2.0"

application {
  mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("io.ktor:ktor-server-core-jvm")
  implementation("io.ktor:ktor-server-auth-jvm")
  implementation("io.ktor:ktor-server-auth-jwt-jvm")
  implementation("io.ktor:ktor-server-call-logging-jvm")
  implementation("io.ktor:ktor-server-content-negotiation-jvm")
  implementation("io.ktor:ktor-client-content-negotiation-jvm")
  implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
  implementation("io.ktor:ktor-server-netty-jvm")

  implementation("ch.qos.logback:logback-classic:$logback")
  implementation("commons-codec:commons-codec:$commonsCodec")
  implementation("org.postgresql:postgresql:$postgres")
  implementation("org.jetbrains.exposed:exposed-core:$exposed")
  implementation("org.jetbrains.exposed:exposed-dao:$exposed")
  implementation("org.jetbrains.exposed:exposed-jdbc:$exposed")
  implementation("org.jetbrains.exposed:exposed-java-time:$exposed")

  testImplementation("io.ktor:ktor-server-tests-jvm")
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin")
}
