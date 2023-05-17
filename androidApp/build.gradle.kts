

plugins {
    id("com.android.application")
    id ("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    kotlin("android")
}

android {
    namespace = "ru.esstu.android"
    compileSdk = 33
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
        kotlinCompilerExtensionVersion = "1.3.2"
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
    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation ("androidx.activity:activity-compose:1.3.1")
    implementation ("androidx.compose.ui:ui:1.2.0")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.2.0")
    implementation ("androidx.compose.material3:material3:1.0.0-alpha11")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.2.0")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.2.0")
    debugImplementation ("androidx.compose.ui:ui-test-manifest:1.2.0")
//    implementation("androidx.compose.ui:ui:1.3.3")
//    implementation("androidx.compose.ui:ui-tooling:1.3.3")
//    implementation("androidx.compose.ui:ui-tooling-preview:1.3.3")
//    implementation("androidx.compose.foundation:foundation:1.3.1")
//    implementation("androidx.activity:activity-compose:1.6.1")
//    implementation ("androidx.appcompat:appcompat:1.6.0")
//    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
//    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
//    implementation ("androidx.core:core:1.9.0")
//    implementation ("androidx.compose.material3:material3:1.2.0-alpha01")
//    //Initializer
//    implementation ("androidx.startup:startup-runtime:1.1.1")
//
//    //compose nav
//
        implementation ("androidx.navigation:navigation-compose:2.5.3")
//
        //Coroutines
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
//
//    //Compose shimmer
        implementation ("com.valentinilk.shimmer:compose-shimmer:1.0.3")
//
//    //Insets
//    implementation ("com.google.accompanist:accompanist-insets-ui:0.23.1")
//    implementation ("com.google.accompanist:accompanist-insets:0.23.1")
//
//    //system ui controller
//    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.28.0")
//
//    //Material
      implementation ("com.google.android.material:material:1.7.0")
//
//    //Time https://docs.korge.org/klock/
    implementation ("com.soywiz.korlibs.klock:klock-android:3.0.1")
//
//    //landscapist glide
    implementation ("com.github.skydoves:landscapist-glide:1.5.2")
//
//    //pager
//    implementation ("com.google.accompanist:accompanist-pager:0.23.1")
//    implementation ("com.google.accompanist:accompanist-pager-indicators:0.23.1")
//    implementation ("com.google.accompanist:accompanist-flowlayout:0.28.0")
//
    //Work Manager
    implementation ("androidx.work:work-runtime-ktx:2.7.1")

    //Hilt
    implementation ("com.google.dagger:hilt-android:2.44")
    kapt ("com.google.dagger:hilt-compiler:2.44")
    implementation ("androidx.hilt:hilt-work:1.0.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")


    implementation ("com.google.accompanist:accompanist-permissions:0.28.0")
}

kapt {
    correctErrorTypes = true
}