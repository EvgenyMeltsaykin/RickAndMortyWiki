import com.wiki.buildsrc.Libs

plugins {
    id("common-plugin")
    id("ui-plugin")
    id("network-plugin")
    id("test-plugin")
}

dependencies {
    implementation(Libs.cicerone)
    implementation(Libs.Koin.core)
    implementation(Libs.Koin.android)
    implementation(Libs.Lifecycle.viewModel)
    implementation(Libs.Lifecycle.runtime)

    implementation(projects.cfNetwork)
    implementation(projects.cfData)
    implementation(projects.cfUi)
}