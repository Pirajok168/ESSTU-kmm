plugins{
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "ru.android.esstu.core.theme"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
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

dependencies{

    implementation ("androidx.compose.material3:material3:1.1.0-alpha07")

    //system ui controller
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.28.0")


}