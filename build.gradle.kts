plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("8.1.0").apply(false)
    id("com.android.library").version("7.2.2").apply(false)
    id("app.cash.sqldelight").version("2.0.0").apply(false)
    id ("org.jetbrains.kotlin.kapt") version "1.9.0" apply false
    kotlin("android").version("1.9.20").apply(false)
    kotlin("multiplatform").version("1.9.20").apply(false)


}
buildscript {
    val sqlDelightVersion = "1.5.3"
    dependencies{
        classpath ("org.jetbrains.kotlin:kotlin-serialization:1.9.20")

        //classpath("app.cash.sqldelight:sqlite-driver:2.0.0")


        classpath("com.android.tools.build:gradle:8.1.2")

        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
    }
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
