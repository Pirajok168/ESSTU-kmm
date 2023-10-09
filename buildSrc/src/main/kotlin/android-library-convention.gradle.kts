import com.android.build.api.dsl.LibraryExtension

plugins {
    id("com.android.library")
    id("android-common-convention")
}

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    compileSdk = Versions.Android.compileSdk
    defaultConfig {
        minSdk = Versions.Android.minSdk
        targetSdk = Versions.Android.targetSdk
    }

}

fun Project.android(configure: Action<LibraryExtension>): Unit =
    extensions.configure("android", configure)
