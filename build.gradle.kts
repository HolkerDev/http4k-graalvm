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
    implementation("org.http4k:http4k-format-kotlinx-serialization:4.27.0.0")
    implementation("software.amazon.awssdk:dynamodb:2.17.209"){
        exclude(group="software.amazon.awssdk", module = "apache-client")
        exclude(group="software.amazon.awssdk", module = "netty-nio-client")
        exclude(group="org.slf4j", module = "slf4j-api")
    }

    implementation("software.amazon.awssdk:url-connection-client:2.17.209")
    implementation("io.microlam:slf4j-simple-lambda:2.0.0-alpha5_1.3")
    implementation("org.slf4j:slf4j-api:1.7.36")
    implementation("org.slf4j:log4j-over-slf4j:2.0.0-alpha1")
    implementation("org.apache.logging.log4j:log4j-api:2.17.2")

    // implementation("io.github.microutils:kotlin-logging:2.1.23")
    // implementation("ch.qos.logback:logback-classic:1.3.0-alpha16")
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