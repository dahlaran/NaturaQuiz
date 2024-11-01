import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.dahlaran.naturaquiz"
    compileSdk = 34

    val file = rootProject.file("local.properties")
    val properties = Properties()
    properties.load(file.inputStream())
    defaultConfig {
        applicationId = "com.dahlaran.naturaquiz"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "API_KEY", "\"${properties.getProperty("trefle.api.key")}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/gradle/incremental.annotation.processors"
        }
    }

}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.androidx.material3)

    // Implement retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)

    // Time library
    implementation (libs.android.joda)

    // Log Library
    implementation(libs.timber)

    // Compose dependencies
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.swiperefresh)
    implementation(libs.kotlinx.serialization.json)

    // Hilt dependencies
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.runtime.livedata)
    kapt(libs.hilt.android.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

    // Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

    // Coil
    implementation(libs.coil.compose)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    testImplementation(libs.junit)
}