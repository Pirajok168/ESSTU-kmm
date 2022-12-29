


plugins{
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.squareup.sqldelight")
}

kotlin{
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

    sourceSets{
        val commonMain by getting{
            dependencies{
                implementation(Dependencies.Coroutines.coroutines)
                implementation(Dependencies.Kotlin.Serialization.serialization)

                implementation(Dependencies.Ktor.client)
                implementation(Dependencies.Ktor.serialization)
                implementation(Dependencies.Ktor.negotiation)
                implementation(Dependencies.Ktor.json)
                implementation(Dependencies.Ktor.logging)

                implementation(Dependencies.Kodein.kodein)

                implementation(Dependencies.Settings.setting)

                implementation(Dependencies.Time.time)

                implementation(Dependencies.SqlDelight.core)

                implementation(Dependencies.Log.log)

                implementation(Dependencies.File.file)

                implementation(Dependencies.Datastore.datastoreCore)
                implementation(Dependencies.Datastore.datastorePreferences)


            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting{
            dependencies{
                implementation(Dependencies.Ktor.Android.client)
                implementation(Dependencies.SqlDelight.android)
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
                implementation(Dependencies.Ktor.Ios.client)
                implementation(Dependencies.SqlDelight.ios)
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

    database("EsstuDatabase"){
        packageName = "ru.esstu.student"
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