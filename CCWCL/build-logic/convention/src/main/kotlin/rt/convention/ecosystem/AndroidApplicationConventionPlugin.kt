package rt.convention.ecosystem

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import rt.convention.ecosystem.ext.configureKotlinAndroid
import rt.convention.ext.getPluginId
import rt.convention.ext.versionCatalog
import rt.convention.ext.versionInt

/**
 * Convention plugin for providing Android application configuration.
 *
 *
 * Adopted from Now in Android:
 * https://github.com/android/nowinandroid/blob/main/build-logic/convention/src/main/kotlin/com/google/samples/apps/nowinandroid/AndroidCompose.kt
 *
 * References:
 * https://docs.gradle.org/current/samples/sample_convention_plugins.html
 */
@Suppress("unused")
open class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val libs = versionCatalog()
            with(pluginManager) {
                apply(libs.getPluginId("android-app"))
                apply(libs.getPluginId("kotlin-android"))
            }

            // Because plugin management happens before catalogs ar defined, at this point
            // we cannot use the type-safe accessors to the version catalogs.
            // Ref: https://melix.github.io/blog/2021/03/version-catalogs-faq.html#_can_i_use_a_version_catalog_to_declare_plugin_versions
            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = libs.versionInt("compileSdk")

                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }

                packaging {
                    resources {
                        excludes += listOf(
                            "/META-INF/{AL2.0,LGPL2.1}",
                            "/META-INF/LICENSE.md",
                            "/META-INF/LICENSE-notice.md"
                        )
                    }
                }
            }
        }
    }
}
