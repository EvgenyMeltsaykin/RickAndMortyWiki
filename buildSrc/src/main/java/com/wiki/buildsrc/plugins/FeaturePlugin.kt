package com.wiki.buildsrc.plugins

import com.wiki.buildsrc.Libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class FeaturePlugin : BasePlugin() {
    override fun apply(target: Project) = target.applyAndroid()

    private fun Project.applyAndroid() {
        plugins.run {
            apply("common-plugin")
            apply("ui-plugin")
            apply("test-plugin")
        }

        android {

            dependencies {
                implementation(Libs.Koin.core)
                implementation(Libs.Koin.android)
                implementation(Libs.cicerone)
                implementation(Libs.Lifecycle.viewModel)

                implementation(project(":cf-core"))
                implementation(project(":cf-data"))
                implementation(project(":cf-network"))
                implementation(project(":cf-ui"))
                implementation(project(":cf-extensions"))
            }
        }
    }


}