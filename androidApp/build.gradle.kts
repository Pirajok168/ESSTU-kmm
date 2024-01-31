

plugins {
    id("com.android.application")
    id ("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    kotlin("android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "ru.esstu.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "ru.esstu.android"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
     buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.ui:ui-tooling:1.5.4")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")
    implementation("androidx.compose.foundation:foundation:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.core:core:1.12.0")

    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    //Initializer
    implementation ("androidx.startup:startup-runtime:1.1.1")

    //compose nav
    implementation ("androidx.navigation:navigation-compose:2.7.4")

    //Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    //Compose shimmer
    implementation ("com.valentinilk.shimmer:compose-shimmer:1.0.3")

    //Insets
    implementation ("com.google.accompanist:accompanist-insets-ui:0.23.1")
    implementation ("com.google.accompanist:accompanist-insets:0.23.1")

    //system ui controller
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.28.0")

    //Material
    implementation ("com.google.android.material:material:1.10.0")

    //Time https://docs.korge.org/klock/
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    //landscapist glide
    implementation ("com.github.skydoves:landscapist-glide:1.5.2")

    //Work Manager
    implementation ("androidx.work:work-runtime-ktx:2.8.1")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    implementation("com.google.firebase:firebase-messaging:23.4.0")
    implementation("com.google.firebase:firebase-crashlytics:18.6.1")
    implementation("com.google.firebase:firebase-analytics:21.5.0")
    kapt("com.google.dagger:hilt-compiler:2.48.1")
    implementation("androidx.hilt:hilt-work:1.0.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    implementation("com.airbnb.android:lottie-compose:6.1.0")
    implementation("com.google.accompanist:accompanist-permissions:0.28.0")

    implementation("org.kodein.di:kodein-di:7.12.0")
}

kapt {
    correctErrorTypes = true
}
