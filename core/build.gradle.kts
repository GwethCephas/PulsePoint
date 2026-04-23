import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrainsKotlinSerialization)
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
        val localPropertiesFile = rootProject.file("local.properties")

        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { properties.load(it) }
        }

        val apiKey = properties.getProperty("API_KEY") ?: System.getenv("MY_API_KEY") ?: ""
        val webClient = properties.getProperty("WEB_CLIENT") ?: System.getenv("WEB_CLIENT") ?: ""

        buildConfigField("String", "API_KEY", "\"$apiKey\"")
        buildConfigField("String", "WEB_CLIENT", "\"$webClient\"")

    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests.all {
            (this as? Test)?.apply {
                maxHeapSize = "2048m"
            }
        }
    }
}

dependencies {
    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.paging.testing)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.turbine)

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