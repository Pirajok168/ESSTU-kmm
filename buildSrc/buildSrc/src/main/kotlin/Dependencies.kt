object Dependencies {

    object Kodein{
        private const val version = "7.12.0"
        const val kodein = "org.kodein.di:kodein-di:$version"
    }

    // key-value data https://github.com/russhwolf/multiplatform-settings
    object Settings{
        private const val version = "0.9"
        const val setting = "com.russhwolf:multiplatform-settings-no-arg:$version"
    }

    //Time https://docs.korge.org/klock/
    object Time{
        private const val version = "3.0.1"
        const val time = "com.soywiz.korlibs.klock:klock:$version"
    }



    object Kotlin {
        private const val version = "1.7.20"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"

        object Serialization {
            const val gradlePlugin = "org.jetbrains.kotlin:kotlin-serialization:1.6.21"
            const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.2.2"
        }

        object Coroutines {
            private const val version = "1.6.4"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        }
    }



    object Ktor{
        private const val ktorVersion = "2.1.1"
        object Android{
            const val client = "io.ktor:ktor-client-okhttp:$ktorVersion"
        }

        object Ios{
            const val client = "io.ktor:ktor-client-ios:$ktorVersion"
        }

        const val client = "io.ktor:ktor-client-core:$ktorVersion"
        const val serialization =  "io.ktor:ktor-client-serialization:$ktorVersion"
        const val negotiation = "io.ktor:ktor-client-content-negotiation:$ktorVersion"
        const val json = "io.ktor:ktor-serialization-kotlinx-json:$ktorVersion"
        const val logging =  "io.ktor:ktor-client-logging:$ktorVersion"
    }

    object SqlDelight {
        private const val version = "1.5.3"

        const val gradlePlugin = "com.squareup.sqldelight:gradle-plugin:$version"
        const val core = "com.squareup.sqldelight:runtime:$version"
        const val android = "com.squareup.sqldelight:android-driver:$version"
        const val ios = "com.squareup.sqldelight:native-driver:$version"
    }

    object Android {
        const val gradlePlugin = "com.android.tools.build:gradle:7.2.2"

    }

    object Coroutines{
        private const val version = "1.6.4"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    }

    object Log{
        private const val version = "2.6.1"
        const val log = "io.github.aakira:napier:$version"
    }

    object File{
        private const val version = "3.2.0"
        const val file = "com.squareup.okio:okio:$version"
    }

    object Datastore{
        private const val version = "1.1.0-dev01"
        const val datastoreCore = "androidx.datastore:datastore-core-okio:$version"
        const val datastorePreferences = "androidx.datastore:datastore-preferences-core:$version"
    }

}

