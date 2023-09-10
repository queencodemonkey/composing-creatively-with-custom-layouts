plugins {
    `kotlin-dsl`
}

group = "rt.build-logic"
version = "1.0-SNAPSHOT"

java {
    val javaVersionInt = libs.versions.java.get().toInt()
    val javaVersion = JavaVersion.toVersion(javaVersionInt)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersionInt))
    }
}

dependencies {
    compileOnly(libs.gradle.plugin.android)
    compileOnly(libs.gradle.plugin.kotlin)
    implementation(libs.gradle.plugin.versionCatalogUpdate)
    implementation(libs.gradle.plugin.versionsBenManes)
}


gradlePlugin {
    plugins {
        // region == Ecosystem Plugins ==
        register("androidLibrary") {
            id = "rt.android.library"
            implementationClass = "rt.convention.ecosystem.AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "rt.android.library.compose"
            implementationClass = "rt.convention.ecosystem.AndroidLibraryComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "rt.android.application"
            implementationClass = "rt.convention.ecosystem.AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "rt.android.application.compose"
            implementationClass = "rt.convention.ecosystem.AndroidApplicationComposeConventionPlugin"
        }
        // endregion

        // region == Plugin Configuration/Customizations ==
        register("versionCatalogUpdate") {
            id = "rt.versionCatalogUpdate"
            implementationClass = "rt.convention.VersionCatalogUpdateConventionPlugin"
        }
        // endregion
    }
}