package com.wiki.buildsrc.plugins

import com.wiki.buildsrc.Libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class TestPlugin : BasePlugin() {
    override fun apply(target: Project) = target.applyAndroid()

    private fun Project.applyAndroid() {
        plugins.run {
            apply("com.android.library")
            apply("common-plugin")
        }

        android {
            dependencies {
                testImplementation(Libs.Test.junit)
                androidTestImplementation(Libs.Test.testJunit)
                androidTestImplementation(Libs.Test.testEspressoCore)
                androidTestImplementation(Libs.Test.compose)
            }
        }
    }

}