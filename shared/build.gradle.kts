
plugins {
    id("multiplatform-common-convention")
}


kotlin {
    sourceSets {
        val ktorVersion = "2.1.1"
        val sqlDelightVersion = "1.5.3"
        val commonMain by getting{
            dependencies{
                //implementation(project(":lib:core"))
                
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

                //serialization

                implementation ("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.2.2")

                implementation(Dependencies.Kodein.kodein)
                implementation(Dependencies.Kotlin.serialization)

                // DI Kodein

                // key-value data https://github.com/russhwolf/multiplatform-settings
                implementation("com.russhwolf:multiplatform-settings-no-arg:0.9")

                //Time https://docs.korge.org/klock/
                implementation ("com.soywiz.korlibs.klock:klock:3.0.1")


                implementation("app.cash.sqldelight:primitive-adapters:2.0.0")

                //Log
                implementation("io.github.aakira:napier:2.6.1")

                //File
                implementation("com.squareup.okio:okio:3.2.0")


                // Lower-level APIs with support for custom serialization
                implementation("androidx.datastore:datastore-core-okio:1.1.0-dev01")
                // Higher-level APIs for storing values of basic types
                implementation("androidx.datastore:datastore-preferences-core:1.1.0-dev01")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting{
            dependencies{
                //Ktor
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")

                //Initializer
                implementation ("androidx.startup:startup-runtime:1.1.1")

                //SqlDelight
                implementation("app.cash.sqldelight:android-driver:2.0.0")
            }
        }
    }
}


sqldelight {
    databases{
        create("EsstuDatabase"){
            packageName.set("ru.esstu.student")
        }
    }
}

android {
    namespace = "ru.esstu"
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
}
