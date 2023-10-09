import com.android.build.gradle.BaseExtension


fun Project.android(configure: BaseExtension.() -> Unit): Unit =
    extensions.configure("android", configure)

android {
    compileOptions {
        sourceCompatibility = Versions.javaVersion
        targetCompatibility = Versions.javaVersion
    }

}

