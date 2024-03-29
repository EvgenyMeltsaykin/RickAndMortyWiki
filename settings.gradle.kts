enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven(url = "https://jitpack.io")

        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven(url = "https://jitpack.io")
        mavenCentral()
    }
}
rootProject.name = "RickAndMortyWiki"
include(":app")
include(":cf-core")
include(":cf-data")
include(":f-list-character")
include(":cf-network")
include(":i-character")
include(":i-episode")
include(":cf-ui")
include(":f-detail-character")
include(":cf-extensions")
include(":f-list-episode")
include(":f-general-adapter")
include(":f-detail-episode")
include(":i-location")
include(":f-list-location")
include(":f-detail-location")
include(":f-search")
