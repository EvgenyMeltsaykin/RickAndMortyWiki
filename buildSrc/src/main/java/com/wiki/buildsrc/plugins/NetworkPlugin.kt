package com.wiki.buildsrc.plugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class NetworkPlugin : BasePlugin() {
    override fun apply(target: Project) = target.applyAndroid()

    private fun Project.applyAndroid() {
        plugins.run {
            apply("common-plugin")
        }

        android {

            dependencies {
                implementation("com.squareup.retrofit2:retrofit:2.9.0")
                implementation("com.squareup.retrofit2:converter-gson:2.9.0")
                implementation("com.squareup.okhttp3:okhttp:4.9.3")
                implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

                implementation("io.insert-koin:koin-core:3.1.6")
                implementation("io.insert-koin:koin-android:3.1.6")

                implementation(project(":cf-data"))
            }
        }

    }

}