@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("rt.android.library")
    id("rt.android.library.compose")
}

android {
    namespace = "rt.compose.kikagaku"
}

dependencies {
    implementation(libs.compose.ui.util)
    testImplementation(libs.junit.core)
}