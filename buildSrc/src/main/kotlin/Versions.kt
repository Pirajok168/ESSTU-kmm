import org.gradle.api.JavaVersion

object Versions {
    val javaVersion: JavaVersion = JavaVersion.VERSION_1_8
    val jvmTarget = JavaVersion.VERSION_1_8.toString()
    object Android {
        const val minSdk = 21
        const val targetSdk = 33
        const val compileSdk = 34
    }

    object Compose {
        const val version = "1.5.3"
        const val compiler = "1.5.3"
    }

    object Hilt {
        const val hilt_version = "2.48.1"
    }



    object Coroutines {
         const val coroutines_versions = "1.7.3"
    }

    object Ktor {
        const val ktorVersion = "2.1.1"
    }

    object Kotlin {
        const val kotlinVersion = "1.9.10"
    }

    object SqlDelight {
        const val version = "2.0.0"
    }

    object Kodein {
        const val verstion = "7.12.0"
    }
}