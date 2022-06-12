import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.wiki.buildsrc.Libs
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

    signingConfigs{
        create("release"){
            keyAlias = "RAM"
            keyPassword = "ncdPZ9tBJ3Esxmbw"
            storeFile = file("rick_and_morty_key.jks")
            storePassword = "ncdPZ9tBJ3Esxmbw"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))
            proguardFile(file("proguard-rules.pro"))
            signingConfig = signingConfigs.getByName("release")
        }

    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    implementation(Libs.Koin.core)
    implementation(Libs.Koin.android)

    implementation(Libs.Ui.AndroidX.core)
    implementation(Libs.Ui.AndroidX.appcompat)
    implementation(Libs.Ui.material)
    implementation(Libs.Ui.constraintLayout)

    testImplementation(Libs.Test.junit)
    androidTestImplementation(Libs.Test.testJunit)
    androidTestImplementation(Libs.Test.testEspressoCore)
    androidTestImplementation(Libs.Test.compose)
    implementation(Libs.Coroutines.core)
    implementation(Libs.Lifecycle.viewModel)
    implementation(Libs.Lifecycle.runtime)

    implementation(Libs.cicerone)
    implementation(Libs.splashscreen)
    implementation(platform(Libs.Firebase.bom))
    implementation(Libs.Firebase.crashlytics)
    implementation(Libs.Firebase.analytics)

    implementation(projects.cfCore)
    implementation(projects.cfData)
    implementation(projects.cfNetwork)
    implementation(projects.cfUi)
    implementation(projects.cfExtensions)
    implementation(projects.fListCharacter)
    implementation(projects.fListEpisode)
    implementation(projects.fListLocation)
    implementation(projects.fDetailCharacter)
    implementation(projects.fDetailEpisode)
    implementation(projects.fDetailLocation)
    implementation(projects.fSearch)
    implementation(projects.iCharacter)
    implementation(projects.iEpisode)
    implementation(projects.iLocation)

}

val currentFormatDate: String
    get() {
        val date = LocalDateTime.now()
        val format = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return date.format(format)
    }
