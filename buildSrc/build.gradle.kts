plugins{
    `kotlin-dsl`
}


repositories{
    mavenCentral()
    mavenLocal()
    google()
}

dependencies{
    implementation(Dependencies.Kotlin.gradlePlugin)
    implementation(Dependencies.Android.gradlePlugin)
    implementation(Dependencies.Kotlin.Serialization.gradlePlugin)
    implementation(Dependencies.SqlDelight.gradlePlugin)
}


kotlin{

    sourceSets.getByName("main").kotlin.srcDir("buildSrc/src/main/kotlin")
}

