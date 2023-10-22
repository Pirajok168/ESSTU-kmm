/*plugins {
    //trick: for the same plugin versions in all sub-modules
    //id("com.android.application").version("8.1.0").apply(false)
   // id("com.android.library").version("7.2.2").apply(false)
    //id("app.cash.sqldelight").version("2.0.0").apply(false)
    //id ("org.jetbrains.kotlin.kapt") version "1.9.0" apply false
   // kotlin("android").version("1.9.10").apply(false)
  //  kotlin("multiplatform").version("1.9.10").apply(false)
}
buildscript {
    val sqlDelightVersion = "1.5.3"
    dependencies{
       /* classpath ("org.jetbrains.kotlin:kotlin-serialization:1.9.10")


        classpath("com.android.tools.build:gradle:8.1.2")

        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")*/
    }
}*/

/*buildscript {
    dependencies{
        classpath("app.cash.sqldelight:gradle-plugin:2.0.0")
    }
}
plugins {
    id("app.cash.sqldelight").version("2.0.0").apply(false)
}*/

allprojects {
    apply(plugin = "kotlin-convention")
}

plugins {

    /*id("com.android.application").version("8.1.0").apply(false)
    id("com.android.library").version("7.2.2").apply(false)
    id("org.jetbrains.kotlin.kapt") version "1.9.0" apply false*/
}
buildscript {


    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
        classpath ("org.jetbrains.kotlin:kotlin-serialization:1.9.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
