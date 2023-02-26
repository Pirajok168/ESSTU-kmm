buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.0")
        classpath( "com.google.dagger:hilt-android-gradle-plugin:2.44.2")
    }
}

plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("7.4.0").apply(false)
    id("com.android.library").version("7.4.0").apply(false)
    kotlin("android").version("1.8.0").apply(false)
    kotlin("multiplatform").version("1.8.0").apply(false)
    kotlin("plugin.serialization") version "1.8.0"
    id("app.cash.sqldelight").version("2.0.0-alpha05").apply(false)
}




tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
