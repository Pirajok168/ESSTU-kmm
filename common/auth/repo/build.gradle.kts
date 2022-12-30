plugins {
    id("multiplatform-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:auth:api"))
                implementation(project(":common:auth:db"))
                implementation(project(":common:core"))
            }
        }

        androidMain {

        }

        iosMain{

        }
    }
}