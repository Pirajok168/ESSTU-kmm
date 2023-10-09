
plugins {
    id("multiplatform-core-convention")
}

kotlin {
    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(project(":shared"))
            }
        }
    }
}