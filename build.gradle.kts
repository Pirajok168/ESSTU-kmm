plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("7.2.2").apply(false)
    id("com.android.library").version("7.2.2").apply(false)
    kotlin("android").version("1.7.20").apply(false)
    kotlin("multiplatform").version("1.7.20").apply(false)
}
buildscript {
    val sqlDelightVersion = "1.5.3"
    dependencies{
        classpath ("org.jetbrains.kotlin:kotlin-serialization:1.7.20")
        classpath ("com.squareup.sqldelight:gradle-plugin:1.5.3")
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion")
        classpath("com.android.tools.build:gradle:7.3.1")
    }
}



tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
