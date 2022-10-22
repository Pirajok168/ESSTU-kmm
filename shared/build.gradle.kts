
plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
}


kotlin {
    android()
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val ktorVersion = "2.1.1"
        val sqlDelightVersion = "1.5.3"
        val commonMain by getting{
            dependencies{
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

                //serialization

                implementation ("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.2.2")

                //Ktor
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")

                // DI Kodein
                implementation("org.kodein.di:kodein-di:7.12.0")

                // key-value data https://github.com/russhwolf/multiplatform-settings
                implementation("com.russhwolf:multiplatform-settings-no-arg:0.9")

                //Time https://docs.korge.org/klock/
                implementation ("com.soywiz.korlibs.klock:klock:3.0.1")

                //SqlDelight
                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")

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
                implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies{
                //Ktor
                implementation("io.ktor:ktor-client-ios:$ktorVersion")

                //SqlDelight
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")


            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

sqldelight {

    database("NewsDatabase") {
        packageName = "ru.esstu.student.news.announcement.db.announcement"
        sourceFolders = listOf("sqldelight")
    }

    database("TimestampDatabase"){
        packageName ="ru.esstu.student.news"
        sourceFolders = listOf("sqldelight2")
    }

    database("MessageWithRelatedTAble"){
        packageName = "ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history"
        sourceFolders = listOf("sqldelight3")
    }

    database("UserMessageTable"){
        packageName = "ru.esstu.student.messaging.dialog_chat.datasources.db.user_message"
        sourceFolders = listOf("kotlin")
    }
}

android {
    namespace = "ru.esstu"
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }
}
