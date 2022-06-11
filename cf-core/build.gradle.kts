plugins {
    id("common-plugin")
    id("ui-plugin")
    id("network-plugin")
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    implementation("com.github.terrakok:cicerone:7.1")

    implementation("io.insert-koin:koin-core:3.1.6")
    implementation("io.insert-koin:koin-android:3.1.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")

    implementation(project(":cf-network"))
    implementation(project(":cf-data"))
    implementation(project(":cf-ui"))

    api("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.5.6")
}