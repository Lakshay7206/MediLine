plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id ("com.google.dagger.hilt.android")
    kotlin("kapt") // for annotation processing
}

android {
    namespace = "com.example.mediline"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mediline"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("com.razorpay:checkout:1.6.33")
    // Hilt core
    implementation ("com.google.dagger:hilt-android:2.51.1")
    implementation(libs.androidx.navigation.common.android)
    kapt ("com.google.dagger:hilt-compiler:2.51.1")

    // (Optional) Hilt + Navigation Compose
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation(platform("com.google.firebase:firebase-bom:34.1.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
// Core navigation for Compose
    implementation("androidx.navigation:navigation-compose:2.8.0")
// (Optional) If you use ViewModels in navigation destinations
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
// (Optional) If you use Hilt with Compose Navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
//   implementation "androidx.lifecycle:lifecycle-viewmodel-compose"
//    implementation "androidx.lifecycle:lifecycle-runtime-ktx"
//    implementation "androidx.compose.material3:material3"
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    // (optional Realtime DB)
    // implementation(libs.firebase.database)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}