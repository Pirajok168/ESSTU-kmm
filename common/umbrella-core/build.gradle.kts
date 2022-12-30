plugins {
    id("multiplatform-setup")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":common:core"))
                implementation(project(":common:auth:repo"))
                implementation(project(":common:auth:db"))
            }
        }

        androidMain {

        }

        iosMain{

        }
    }
}