import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0-RC"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("plugin.serialization") version "1.7.0-RC"
}

group = "org.example"
version = ""

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.http4k:http4k-serverless-lambda-runtime:4.27.0.0")
    // implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    // implementation("org.htt4k:http4k-format-kotlinx-serialization:4.27.0.0")
    implementation("org.http4k:http4k-format-kotlinx-serialization:4.25.14.0")

}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

tasks.withType<ShadowJar> {
    manifest.attributes("Main-Class" to "MainKt")
    archiveBaseName.set(project.name)
    mergeServiceFiles()
}