package com.wiki.buildsrc.plugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class UiPlugin : BasePlugin() {
    override fun apply(target: Project) = target.applyAndroid()

    private fun Project.applyAndroid() {
        plugins.run {
            apply("com.android.library")
            apply("common-plugin")
        }

        android {

            dependencies {

            }
        }

    }

}