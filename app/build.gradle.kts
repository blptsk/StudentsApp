plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "ru.tinkoff.favouritepersons"
    compileSdk = 36

    defaultConfig {
        applicationId = "ru.tinkoff.favouritepersons"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.google.material)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.gson)

    implementation(libs.rxjava)

    implementation(libs.lifecycle.extensions)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)

    implementation(libs.databinding.base)

    implementation(libs.glide)
    implementation(libs.kotlinx.serialization.json)

    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.converter)
    implementation(libs.okhttp.interceptor)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Hilt
    implementation(libs.hilt)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    androidTestImplementation(libs.hilt.testing)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.compiler.android)
    kaptAndroidTest(libs.hilt.compiler.android)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Test
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.junit.ext.ktx)
    androidTestImplementation(libs.uiautomator)
    androidTestImplementation(libs.compose.ui.test.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.kaspresso)
    androidTestImplementation(libs.kaspresso.allure)
    androidTestImplementation(libs.kaspresso.compose)
    androidTestImplementation(libs.allure.kotlin.model)
    androidTestImplementation(libs.allure.kotlin.commons)
    androidTestImplementation(libs.allure.kotlin.junit4)
    androidTestImplementation(libs.allure.kotlin.android)
    androidTestImplementation(libs.wiremock)
    androidTestImplementation(libs.compose.testing.junit4)
    debugImplementation(libs.compose.testing.manifest)
    testImplementation(kotlin("test"))

}
