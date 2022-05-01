plugins {
    id("ui-plugin")
}
dependencies {
    implementation(project(":cf-core"))
    implementation(project(":cf-ui"))
    implementation(project(":cf-data"))

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
}