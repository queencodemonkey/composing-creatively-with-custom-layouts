package rt.convention.ecosystem.ext


import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import rt.convention.ext.library
import rt.convention.ext.version
import rt.convention.ext.versionCatalog
import rt.convention.ext.versionInt

//
// This file contains extension methods for configuring Kotlin options
// in Android plugins.
//

/**
 * Configure Android ecosystem plugins, including Kotlin options.
 *
 * Adapted from Now in Android:
 * https://github.com/android/nowinandroid/blob/main/build-logic/convention/src/main/kotlin/com/google/samples/apps/nowinandroid/KotlinAndroid.kt#L32
 *
 * @param commonExtension reference to the common properties of the Android plugins (application, library, etc.)
 * @param versionCatalog name of the version catalog to use to resolve versions, deps, plugins, etc.;
 * defaults to name of default version catalog: "libs".
 * Reference: https://docs.gradle.org/current/userguide/platforms.html#sub:conventional-dependencies-toml
 */
internal fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *>,
    versionCatalog: String = "libs",
) {
    val libs = versionCatalog(versionCatalog)

    commonExtension.apply {
        val javaVersion = JavaVersion.toVersion(libs.version("java"))

        compileSdk = libs.versionInt("compileSdk")
        defaultConfig {
            minSdk = libs.versionInt("minSdk")

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            vectorDrawables {
                useSupportLibrary = true
            }
        }

        compileOptions {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
            isCoreLibraryDesugaringEnabled = true
        }

        kotlinOptions {
            // Treat all Kotlin warnings as errors (disabled by default)
            // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
            val warningsAsErrors: String? by project
            allWarningsAsErrors = warningsAsErrors.toBoolean()

            // https://kotlinlang.org/docs/gradle-compiler-options.html#how-to-define-options
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-opt-in=kotlin.ExperimentalStdlibApi"
            )

            jvmTarget = javaVersion.toString()
        }
    }

    dependencies {
        val bom = platform(libs.library("kotlin-bom"))
        add("implementation", bom)

        // https://developer.android.com/studio/write/java8-support#library-desugaring
        add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())
    }
}

/**
 * Extension method to provide block-syntax for setting `kotlinOptions` extensions
 * from any of the common Android extensions.
 */
fun CommonExtension<*, *, *, *, *>.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
    (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}