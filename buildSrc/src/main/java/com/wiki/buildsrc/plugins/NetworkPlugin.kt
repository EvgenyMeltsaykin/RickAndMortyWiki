package com.wiki.buildsrc.plugins

import com.wiki.buildsrc.Libs
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
                implementation(Libs.Retrofit.retrofit)
                implementation(Libs.Retrofit.convertorGson)
                implementation(Libs.OkHttp3.okHttp)
                implementation(Libs.OkHttp3.loggingInterceptor)
                implementation(Libs.Coroutines.core)

                api(Libs.Koin.core)
                api(Libs.Koin.android)

                implementation(project(":cf-data"))
                implementation(project(":cf-extensions"))
            }
        }

    }

}