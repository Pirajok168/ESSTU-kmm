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
include(
    ":lib:core",
    ":lib:features:news:announcement",
    ":lib:features:news:announcement:data",
    ":lib:features:news:announcement:domain",
)