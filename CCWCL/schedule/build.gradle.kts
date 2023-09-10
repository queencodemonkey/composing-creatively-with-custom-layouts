@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("rt.android.library")
    id("rt.android.library.compose")
}

android {
    namespace = "rt.compose.schedule"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.compose.ui.foundation)
    implementation(libs.compose.ui.material3)
    api(libs.kotlin.collections.immutable)
}