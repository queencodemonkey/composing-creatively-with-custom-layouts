package rt.convention.ext

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

//
// This file contains extension methods related to Gradle project instances
//

/**
 * Returns the [VersionCatalog] where the name matches [catalog].
 *
 * The default [catalog] passed is name of the default Version Catalog
 * ("libs.versions.toml").
 */
internal fun Project.versionCatalog(catalog: String = "libs") =
    extensions.getByType<VersionCatalogsExtension>().named(catalog)
