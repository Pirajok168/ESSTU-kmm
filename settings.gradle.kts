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
//include(":androidApp")
//include(":shared")

include(":core:theme")





include(":feature:news")