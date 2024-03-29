plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("common-plugin") {
            id = "common-plugin"
            implementationClass = "com.wiki.buildsrc.plugins.CommonPlugin"
        }
        register("feature-plugin") {
            id = "feature-plugin"
            implementationClass = "com.wiki.buildsrc.plugins.FeaturePlugin"
        }

        register("ui-plugin") {
            id = "ui-plugin"
            implementationClass = "com.wiki.buildsrc.plugins.UiPlugin"
        }

        register("network-plugin") {
            id = "network-plugin"
            implementationClass = "com.wiki.buildsrc.plugins.NetworkPlugin"
        }

        register("test-plugin") {
            id = "test-plugin"
            implementationClass = "com.wiki.buildsrc.plugins.TestPlugin"
        }

    }
}
repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
    google()
}

dependencies {
    implementation("com.squareup:kotlinpoet:1.11.0")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
    implementation("com.android.tools.build:gradle:7.1.3")
}
