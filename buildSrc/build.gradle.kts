
import java.io.FileInputStream
import java.util.*

plugins {
    `kotlin-dsl`
    kotlin("multiplatform").version("1.9.10").apply(false)
    id("app.cash.sqldelight").version("2.0.0").apply(false)
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation(kotlin("gradle-plugin", version = "1.9.10"))
    implementation("com.android.tools.build:gradle:8.1.2")
    implementation("org.jetbrains.kotlin:kotlin-serialization:1.9.10")
    implementation("app.cash.sqldelight:gradle-plugin:2.0.0")
}


