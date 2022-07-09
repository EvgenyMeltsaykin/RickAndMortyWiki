package com.wiki.buildsrc

object Versions {
    const val kotlin = "1.6.21"
    const val koin = "3.2.0"
    const val firebaseBom = "29.3.1"
    const val cicerone = "7.1"
    const val androidxCore = "1.8.0"
    const val androidxAppcompat = "1.4.2"
    const val material = "1.6.1"
    const val constraintLayout = "2.1.4"
    const val junit = "4.13.2"
    const val testJunit = "1.1.3"
    const val testEspresso = "3.4.0"
    const val testCompose = "1.1.1"
    const val coroutinesCore = "1.6.1"
    const val androidxLifecycle = "2.4.1"
    const val splashscreen = "1.0.0-rc01"
    const val swipeRefresh = "1.1.0"
    const val glide = "4.13.0"
    const val glideTransformations = "4.3.0"
    const val retrofit = "2.9.0"
    const val okhttp = "4.9.3"
    const val delegateAdapter = "4.3.2"
    const val viewBindingDelegate = "1.5.6"
}

object Libs {

    const val cicerone = "com.github.terrakok:cicerone:${Versions.cicerone}"
    const val splashscreen = "androidx.core:core-splashscreen:${Versions.splashscreen}"
    const val viewBindingDelegate = "com.github.kirich1409:viewbindingpropertydelegate:1.5.6"

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
        const val crashlytics = "com.google.firebase:firebase-crashlytics"
        const val analytics = "com.google.firebase:firebase-analytics"
    }

    object Koin {
        const val core = "io.insert-koin:koin-core:${Versions.koin}"
        const val android = "io.insert-koin:koin-android:${Versions.koin}"
    }

    object Ui {

        object AndroidX {
            const val core = "androidx.core:core-ktx:${Versions.androidxCore}"
            const val appcompat = "androidx.appcompat:appcompat:${Versions.androidxAppcompat}"
        }

        const val material = "com.google.android.material:material:${Versions.material}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefresh}"

    }

    object Glide {
        const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val glideCompile = "com.github.bumptech.glide:compiler:${Versions.glide}"
        const val glideTransformations = "jp.wasabeef:glide-transformations:${Versions.glideTransformations}"
    }

    object Coroutines {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCore}"
    }

    object Lifecycle {
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidxLifecycle}"
        const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.androidxLifecycle}"
    }

    object Test {
        const val junit = "junit:junit:${Versions.junit}"
        const val testJunit = "androidx.test.ext:junit:${Versions.testJunit}"
        const val testEspressoCore = "androidx.test.espresso:espresso-core:${Versions.testEspresso}"
        const val compose = "androidx.compose.ui:ui-test-junit4:${Versions.testCompose}"
    }

    object Retrofit {
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        const val convertorGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    }

    object OkHttp3 {
        const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    }

    object DelegateAdapter{
        const val adapter = "com.hannesdorfmann:adapterdelegates4-kotlin-dsl:${Versions.delegateAdapter}"
        const val viewBinding = "com.hannesdorfmann:adapterdelegates4-kotlin-dsl-viewbinding:${Versions.delegateAdapter}"
    }

}

