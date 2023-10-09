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
                Dependencies.Ktor.allDependencies.forEach {
                    implementation(it)
                }

                implementation("app.cash.sqldelight:primitive-adapters:2.0.0")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependencies.Ktor.Android.okhttpp)

                implementation("app.cash.sqldelight:android-driver:2.0.0")
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
                implementation(Dependencies.Ktor.IOS.native_ios)
                implementation("app.cash.sqldelight:native-driver:2.0.0")
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

