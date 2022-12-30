pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()

    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()

    }
}

rootProject.name = "ESSTU"
include(":androidApp")
include(":shared")


include(":common:core")
include(":common:auth:api")
include(":common:auth:db")
include(":common:auth:repo")
include(":common:umbrella-core")