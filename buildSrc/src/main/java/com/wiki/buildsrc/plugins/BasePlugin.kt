package com.wiki.buildsrc.plugins

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.file.ConfigurableFileTree
import org.gradle.kotlin.dsl.get


fun Project.android(action: BaseExtension.() -> Unit) =
    (extensions["android"] as BaseExtension).run(action)

abstract class BasePlugin : Plugin<Project> {

    protected fun DependencyHandler.implementation(depName: String) {
        add("implementation", depName)
    }

    protected fun DependencyHandler.annotationProcessor(depName: String) {
        add("annotationProcessor", depName)
    }

    protected fun DependencyHandler.kapt(depName: String) {
        add("kapt", depName)
    }

    protected fun DependencyHandler.api(depName: String) {
        add("api", depName)
    }

    protected fun DependencyHandler.implementation(fileTree: ConfigurableFileTree) {
        add("implementation", fileTree)
    }

    fun DependencyHandler.implementation(project: ProjectDependency) {
        add("implementation", project)
    }

    fun DependencyHandler.testImplementation(project: Project) {
        add("testImplementation", project)
    }

    fun DependencyHandler.testImplementation(depName: String) {
        add("testImplementation", depName)
    }

    fun DependencyHandler.androidTestImplementation(depName: String) {
        add("androidTestImplementation", depName)
    }

}
