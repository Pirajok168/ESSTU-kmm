import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("app.cash.sqldelight")
}

kotlin {
    androidTarget()

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

        val commonMain by getting {
            dependencies {
                implementation(project(":lib:core"))
                implementation(project(":shared"))
                implementation(Dependencies.Kodein.kodein)
                implementation(Dependencies.Ktor.ktore_client)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {



            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {

            }
        }
    }
}


sqldelight {

}

android {
    namespace = "ru.esstu"
    compileSdk = Versions.Android.compileSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Versions.Android.minSdk
        targetSdk =  Versions.Android.targetSdk
    }
}

