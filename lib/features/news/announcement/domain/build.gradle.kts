plugins {
    id("multiplatform-domain-convention")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":lib:features:news:announcement:data"))
                implementation(project(":shared"))
                implementation(Dependencies.Kotlin.dateTime)
            }
        }
    }
}

android{
    namespace = "ru.esstu.news.announcement.domain"
}
