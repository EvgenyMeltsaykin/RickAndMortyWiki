buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.6.20")
        //classpath ("com.google.gms:google-services:4.3.10")
        //classpath ("com.google.firebase:firebase-crashlytics-gradle:2.8.1")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
