package  rt.convention.ecosystem

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import rt.convention.ecosystem.ext.configureAndroidCompose
import rt.convention.ext.getPluginId
import rt.convention.ext.versionCatalog

/**
 * Convention Plugin for configuring Compose in library modules.
 *
 * Adopted from Now in Android:
 * https://github.com/android/nowinandroid/blob/main/build-logic/convention/src/main/kotlin/com/google/samples/apps/nowinandroid/AndroidCompose.kt
 *
 * References:
 * https://docs.gradle.org/current/samples/sample_convention_plugins.html
 */
@Suppress("unused")
class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(versionCatalog().getPluginId("android-app"))
            configureAndroidCompose(extensions.getByType<ApplicationExtension>())
        }
    }
}
