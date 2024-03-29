package com.wiki.buildsrc.plugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CommonPlugin : BasePlugin() {
    override fun apply(target: Project) = target.applyAndroid()

    private fun Project.applyAndroid() {
        plugins.run {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
        }

        android {
            compileSdkVersion(31)
            defaultConfig {
                minSdk = 26
            }
            buildFeatures.viewBinding = true

            dependencies {

            }
        }
    }
}
