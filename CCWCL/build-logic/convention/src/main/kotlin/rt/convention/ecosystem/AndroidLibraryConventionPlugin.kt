package rt.convention.ecosystem

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import rt.convention.ecosystem.ext.configureKotlinAndroid
import rt.convention.ext.getPluginId
import rt.convention.ext.versionCatalog

/**
 * Convention Plugin for providing common Android library configuration.
 *
 *
 * Adopted from Now in Android:
 * https://github.com/android/nowinandroid/blob/main/build-logic/convention/src/main/kotlin/com/google/samples/apps/nowinandroid/AndroidCompose.kt
 *
 * References:
 * https://docs.gradle.org/current/samples/sample_convention_plugins.html
 */
@Suppress("unused")
open class AndroidLibraryConventionPlugin : Plugin<Project> {
	override fun apply(target: Project) {
		with(target) {
			val libs = versionCatalog()
			with(pluginManager) {
				apply(libs.getPluginId("android-lib"))
				apply(libs.getPluginId("kotlin-android"))
			}

			// Because plugin management happens before catalogs ar defined, at this point
			// we cannot use the type-safe accessors to the version catalogs.
			// Ref: https://melix.github.io/blog/2021/03/version-catalogs-faq.html#_can_i_use_a_version_catalog_to_declare_plugin_versions
			extensions.configure<LibraryExtension> {
				configureKotlinAndroid(this)

				defaultConfig {
					consumerProguardFiles("consumer-rules.pro")
				}

				buildTypes {
					release {
						isMinifyEnabled = false
						proguardFiles(
							getDefaultProguardFile("proguard-android-optimize.txt"),
							"proguard-rules.pro"
						)
					}
				}
			}
		}
	}
}
