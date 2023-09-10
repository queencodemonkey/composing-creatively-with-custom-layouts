@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("rt.android.application")
    id("rt.android.application.compose")
}

android {
    namespace = "rt.compose.custom"

    defaultConfig {
        applicationId = "rt.compose.custom"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(project(":imeji"))
    implementation(project(":custom-modifier"))
    implementation(project(":schedule"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.ui.material3)
    implementation(libs.compose.animation.core)
    implementation(libs.compose.animation.graphics)
}