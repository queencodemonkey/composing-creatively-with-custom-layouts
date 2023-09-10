@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  id("rt.android.library")
  id("rt.android.library.compose")
}

android {
  namespace = "rt.compose.custom.modifier"
}

dependencies {
  implementation(libs.compose.ui.util)
  implementation(libs.compose.ui.material3)
  testImplementation(libs.junit.core)
}