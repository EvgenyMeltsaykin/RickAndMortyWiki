import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    //id("com.google.gms.google-services")
    // id("com.google.firebase.crashlytics")
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

    signingConfigs {
        /*
        create("release") {
            keyAlias = "RAMW"
            keyPassword = "rick_and_morty_wiki"
            storeFile = file("rick_and_morty_wiki")
            storePassword = "rick_and_morty_wiki"
        }

         */
    }

    buildTypes {
        release {
            //signingConfig = signingConfigs.getByName("release")
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
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.1")

    // Koin Core features

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("io.insert-koin:koin-core:3.1.6")
    implementation("io.insert-koin:koin-android:3.1.6")
    implementation("com.github.terrakok:cicerone:7.1")
    implementation(project(":cf-core"))
    implementation(project(":cf-data"))
    implementation(project(":cf-network"))
    implementation(project(":f-character-list"))
    //implementation(platform("com.google.firebase:firebase-bom:29.3.1"))

    //implementation("com.google.firebase:firebase-crashlytics")
    //implementation("com.google.firebase:firebase-analytics")

}

val currentFormatDate: String
    get() {
        val date = LocalDateTime.now()
        val format = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return date.format(format)
    }
