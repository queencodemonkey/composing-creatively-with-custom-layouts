package rt.convention.ecosystem.ext


import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import rt.convention.ext.library
import rt.convention.ext.version
import rt.convention.ext.versionCatalog

//
// This file contains extension methods for configuring Jetpack Compose
// in Android plugins.
//

/**
 * Configure Compose-specific options for a [Project] that has an
 * Android plugin (library, application, etc.) applied.
 *
 * Adapted from Now in Android:
 * https://github.com/android/nowinandroid/blob/main/build-logic/convention/src/main/kotlin/com/google/samples/apps/nowinandroid/AndroidCompose.kt
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    val libs = versionCatalog()
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = libs.version("compose-compiler")
        }

        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf(
            )
        }

        dependencies {
            val bom = platform(libs.library("compose-bom"))
            add("implementation", bom)
            add("implementation", libs.library("compose-ui-core"))

            // Android Studio Preview support
            add("implementation", libs.library("compose-ui-uiToolingPreview"))
            add("debugImplementation", libs.library("compose-ui-uiTooling"))
        }
    }
}