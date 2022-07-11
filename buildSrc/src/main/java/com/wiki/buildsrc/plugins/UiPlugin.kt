package com.wiki.buildsrc.plugins

import com.wiki.buildsrc.Libs
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
                api(Libs.Ui.AndroidX.core)
                api(Libs.Ui.AndroidX.appcompat)
                api(Libs.Ui.material)
                api(Libs.Ui.swipeRefreshLayout)
                api(Libs.Glide.glide)
                annotationProcessor(Libs.Glide.glideCompile)
                api(Libs.Glide.glideTransformations)
                api(Libs.DelegateAdapter.adapter)
                api(Libs.DelegateAdapter.viewBinding)
                api(Libs.recyclerDecoration)
            }
        }
    }

}