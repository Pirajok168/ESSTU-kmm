plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")

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
        val commonMain by getting{
            dependencies{
                //Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

                //Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.0")

                //Ktor
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                // DI Kodein
                implementation("org.kodein.di:kodein-di:7.12.0")

                // key-value data https://github.com/russhwolf/multiplatform-settings
                implementation("com.russhwolf:multiplatform-settings:0.9")

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
            dependencies{
                //Ktor
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
            }

        }
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