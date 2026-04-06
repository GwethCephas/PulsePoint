import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.gms.google.services)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}
android {
    namespace = "com.ceph.core"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        properties.load(rootProject.file("local.properties").inputStream())
        buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))
        buildConfigField("String", "WEB_CLIENT", properties.getProperty("WEB_CLIENT"))
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)

    // Room
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Google Fonts
    implementation(libs.androidx.ui.text.google.fonts)

    // Credential Manager
    implementation(libs.credential.manager)
    implementation(libs.google.id.service)
    implementation(libs.credential.service.auth)

    // Firebase
    implementation(libs.play.services.auth)
    implementation(libs.firebase.auth)

    // Paging
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.room.paging)

    // Coil
    implementation(libs.coil.compose)

    // Worker
    implementation(libs.work.manager)

    // Navigation
    implementation(libs.androidx.navigation.compose)



}