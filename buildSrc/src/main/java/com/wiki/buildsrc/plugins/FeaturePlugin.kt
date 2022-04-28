package com.wiki.buildsrc.plugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class FeaturePlugin : BasePlugin() {
    override fun apply(target: Project) = target.applyAndroid()

    private fun Project.applyAndroid() {
        plugins.run {
            apply("com.android.library")
            apply("common-plugin")
        }

        android {

            dependencies {
                implementation("androidx.core:core-ktx:1.7.0")
                implementation("androidx.appcompat:appcompat:1.4.1")
                implementation("com.google.android.material:material:1.5.0")
                implementation("androidx.constraintlayout:constraintlayout:2.1.3")

                implementation("io.insert-koin:koin-core:3.1.6")
                implementation("io.insert-koin:koin-android:3.1.6")
                implementation("com.github.terrakok:cicerone:7.1")
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")

                implementation("jp.wasabeef:glide-transformations:4.3.0")

                implementation("com.github.bumptech.glide:glide:4.13.0")
                annotationProcessor("com.github.bumptech.glide:compiler:4.13.0")
                implementation(project(":cf-core"))
                implementation(project(":cf-data"))
                implementation(project(":cf-network"))
                implementation(project(":cf-ui"))
            }
        }
    }


}