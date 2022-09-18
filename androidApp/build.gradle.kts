plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "ru.esstu.android"
    compileSdk = 32
    defaultConfig {
        applicationId = "ru.esstu.android"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:1.2.1")
    implementation("androidx.compose.ui:ui-tooling:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.1")
    implementation("androidx.compose.foundation:foundation:1.2.1")
    implementation("androidx.compose.material:material:1.2.1")
    implementation("androidx.activity:activity-compose:1.5.1")
    implementation ("androidx.appcompat:appcompat:1.5.1")

    //Initializer
    implementation ("androidx.startup:startup-runtime:1.1.1")

    //compose nav
    implementation ("androidx.navigation:navigation-compose:2.4.2")

    //Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    //Compose shimmer
    implementation ("com.valentinilk.shimmer:compose-shimmer:1.0.3")

    //Insets
    implementation ("com.google.accompanist:accompanist-insets-ui:0.23.1")
    implementation ("com.google.accompanist:accompanist-insets:0.23.1")

    //system ui controller
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.23.1")

    //Material
    implementation ("com.google.android.material:material:1.6.1")

    //Time https://docs.korge.org/klock/
    implementation ("com.soywiz.korlibs.klock:klock-android:3.0.1")

    //landscapist glide
    implementation ("com.github.skydoves:landscapist-glide:1.5.2")

    //pager
    implementation ("com.google.accompanist:accompanist-pager:0.23.1")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.23.1")
    implementation ("com.google.accompanist:accompanist-flowlayout:0.23.1")
}