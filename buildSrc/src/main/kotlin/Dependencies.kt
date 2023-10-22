
object Dependencies {

    object Compose {

        const val compose_ui = "androidx.compose.ui:ui:${Versions.Compose.version}"
        const val ui_tooling = "androidx.compose.ui:ui-tooling:${Versions.Compose.version}"
        const val ui_tooling_preview = "androidx.compose.ui:ui-tooling-preview:${Versions.Compose.version}"
        const val compose_foundation = "androidx.compose.foundation:foundation:${Versions.Compose.version}"
        const val compose_material ="androidx.compose.material:material:${Versions.Compose.version}"
    }

    object Hilt {

        const val hilt = "com.google.dagger:hilt-android:${Versions.Hilt.hilt_version}"
        const val kapt_hilt = "com.google.dagger:hilt-compiler:${Versions.Hilt.hilt_version}"
        const val hilt_work = "androidx.hilt:hilt-work:1.0.0"
    }

    object Navigation {
        const val hilt_navigation = "androidx.hilt:hilt-navigation-compose:1.0.0"
        const val navigation_compose = "androidx.navigation:navigation-compose:2.7.4"
    }

    object Coroutines {
        private const val coroutines_versions = "1.7.3"

        const val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Coroutines.coroutines_versions}"
        const val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Coroutines.coroutines_versions}"
    }

    object Ktor {
        const val ktore_client = "io.ktor:ktor-client-core:${Versions.Ktor.ktorVersion}"
        const val client_serializ = "io.ktor:ktor-client-serialization:${Versions.Ktor.ktorVersion}"
        const val context_negotiation = "io.ktor:ktor-client-content-negotiation:${Versions.Ktor.ktorVersion}"
        const val kotlinx_json = "io.ktor:ktor-serialization-kotlinx-json:${Versions.Ktor.ktorVersion}"
        const val loging = "io.ktor:ktor-client-logging:${Versions.Ktor.ktorVersion}"

        object Android {
            const val okhttpp = "io.ktor:ktor-client-okhttp:${Versions.Ktor.ktorVersion}"
        }

        object IOS {
            const val native_ios = "io.ktor:ktor-client-ios:${Versions.Ktor.ktorVersion}"
        }

        val allDependencies =
            listOf(ktore_client, client_serializ, context_negotiation, kotlinx_json, loging)
    }

    object SqlDelight {
        const val plugin = "app.cash.sqldelight:gradle-plugin:${Versions.SqlDelight.version}"

        const val primitive = "app.cash.sqldelight:primitive-adapters:${Versions.SqlDelight.version}"

        object Android {
            const val driver =  "app.cash.sqldelight:android-driver:${Versions.SqlDelight.version}"
        }

        object Ios {
            const val driver =  "app.cash.sqldelight:native-driver:${Versions.SqlDelight.version}"
        }
    }

    object Kotlin {
        const val serialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.Kotlin.kotlinVersion}"
        const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.Kotlin.dateTime}"
    }

    object Kodein {
        const val kodein = "org.kodein.di:kodein-di:${Versions.Kodein.verstion}"
    }
    object Modules {
        val featureNews = ":lib:features:news:announcement"
        val featureNewsData = ":lib:features:news:announcement:data"
        val featureNewsDomain = ":lib:features:news:announcement:domain"
    }
}