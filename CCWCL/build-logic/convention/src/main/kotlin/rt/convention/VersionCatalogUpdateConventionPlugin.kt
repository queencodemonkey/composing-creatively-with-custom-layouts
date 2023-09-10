package rt.convention

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import nl.littlerobots.vcu.plugin.versionCatalogUpdate
import org.gradle.api.Plugin
import org.gradle.api.Project
import rt.convention.ext.getPluginId
import rt.convention.ext.versionCatalog

/**
 * Convention Plugin for configuring "Version Catalog Update" plugin and the requisite
 * "Gradle Versions" plugin by Ben Manes.
 *
 * Plugins:
 * https://github.com/littlerobots/version-catalog-update-plugin
 * https://github.com/ben-manes/gradle-versions-plugin
 *
 * References:
 * https://docs.gradle.org/current/samples/sample_convention_plugins.html
 */
@Suppress("unused")
class VersionCatalogUpdateConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                val libs = versionCatalog()
                apply(libs.getPluginId("versionsBenManes"))
                apply(libs.getPluginId("versionCatalogUpdate"))
            }

            versionCatalogUpdate {
                // Group dependencies by functionality (more or less).
                sortByKey.set(false)
            }

            /**
             * If we are on a stable version, reject an update proposal if it's a non-stable
             * version. We can certainly override this rule and update to a non-stable manually
             * but for the purposes of maintenance updates, omit them.
             */
            tasks.withType(DependencyUpdatesTask::class.java) {
                rejectVersionIf {
                    candidate.version.isNonStableVersion && !currentVersion.isNonStableVersion
                }
            }
        }
    }

    companion object {
        // Heuristics for "unstable" releases.
        private val nonStableKeywords = listOf("ALPHA")

        /**
         * Determines heuristically if a version presented by the "gradle-versions" plugin
         * is "non-stable".
         */
        private val String.isNonStableVersion: Boolean
            get() {
                val uppercase = this.uppercase()
                return nonStableKeywords.any { keyword -> uppercase.contains(keyword) }
            }
    }
}

