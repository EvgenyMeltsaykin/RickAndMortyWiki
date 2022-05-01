package com.wiki.buildsrc.plugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class FeaturePlugin : BasePlugin() {
    override fun apply(target: Project) = target.applyAndroid()

    private fun Project.applyAndroid() {
        plugins.run {
            apply("common-plugin")
            apply("ui-plugin")
        }

        android {

            dependencies {

                implementation("io.insert-koin:koin-core:3.1.6")
                implementation("io.insert-koin:koin-android:3.1.6")
                implementation("com.github.terrakok:cicerone:7.1")
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
                implementation(project(":cf-core"))
                implementation(project(":cf-data"))
                implementation(project(":cf-network"))
                implementation(project(":cf-ui"))
                implementation(project(":cf-extensions"))
            }
        }
    }


}