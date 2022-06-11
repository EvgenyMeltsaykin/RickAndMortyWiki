import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.wiki.rickandmortywiki"
        minSdk = 26
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        applicationVariants.all {
            outputs.map { it as BaseVariantOutputImpl }.forEach {
                it.outputFileName =
                    "Rick And Morty Wiki $versionName ($versionCode)-${buildType.name}-${currentFormatDate}.apk"
            }
        }

    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))
            proguardFile(file("proguard-rules.pro"))
        }

    }
    compileOptions {
        sourceCompatibility(1.11)
        targetCompatibility(1.11)
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")

    implementation("io.insert-koin:koin-core:3.1.6")
    implementation("io.insert-koin:koin-android:3.1.6")
    implementation("com.github.terrakok:cicerone:7.1")
    implementation(project(":cf-core"))
    implementation(project(":cf-data"))
    implementation(project(":cf-network"))
    implementation(project(":cf-ui"))
    implementation(project(":cf-extensions"))
    implementation(project(":f-list-character"))
    implementation(project(":f-list-episode"))
    implementation(project(":f-list-location"))
    implementation(project(":f-detail-episode"))
    implementation(project(":f-detail-character"))
    implementation(project(":f-detail-location"))
    implementation(project(":f-search"))
    implementation(project(":i-character"))
    implementation(project(":i-episode"))
    implementation(project(":i-location"))

    implementation("androidx.core:core-splashscreen:1.0.0-rc01")
    implementation(platform("com.google.firebase:firebase-bom:29.3.1"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.airbnb.android:lottie:4.1.0")
    implementation("com.gitee.zackratos:UltimateBarX:0.8.0")
//modo core
    implementation("com.github.terrakok:modo:0.6.4")
    //for navigation based on FragmentManager
    implementation("com.github.terrakok:modo-render-android-fm:0.6.4")
    implementation("com.github.terrakok:cicerone:7.1")
}

val currentFormatDate: String
    get() {
        val date = LocalDateTime.now()
        val format = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return date.format(format)
    }
