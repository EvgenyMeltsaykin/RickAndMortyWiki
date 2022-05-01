package com.wiki.buildsrc.plugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class UiPlugin : BasePlugin() {
    override fun apply(target: Project) = target.applyAndroid()

    private fun Project.applyAndroid() {
        plugins.run {
            apply("com.android.library")
            apply("common-plugin")
        }

        android {

            dependencies {
                implementation(project(":cf-extensions"))
                implementation("androidx.core:core-ktx:1.7.0")
                implementation("androidx.appcompat:appcompat:1.4.1")
                implementation("com.google.android.material:material:1.5.0")

                implementation("jp.wasabeef:glide-transformations:4.3.0")

                implementation("com.github.bumptech.glide:glide:4.13.0")
                annotationProcessor("com.github.bumptech.glide:compiler:4.13.0")
                implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")


            }
        }

    }

}