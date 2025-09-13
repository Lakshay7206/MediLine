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
    implementation("io.coil-kt:coil-compose:2.6.0") // latest stable

    implementation("androidx.compose.material:material-icons-extended:<version>")
    implementation("androidx.compose.material3:material3:1.1.1") // or latest
    implementation("androidx.compose.material3:material3:1.2.0")

    implementation (platform("androidx.compose:compose-bom:2025.01.00") )// or your version
    implementation ("androidx.compose.material:material-icons-extended")


    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    // Compose UI
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)

    // Material3
    implementation(libs.androidx.material3)

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.0")
    implementation(libs.androidx.navigation.common.android)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0") // optional Hilt + Nav Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3") // optional ViewModel in Nav
    implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.2")

    // Firebase
// Firebase Bill of Materials
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

// Firebase services with KTX (Kotlin extensions)
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    //implementation("com.google.firebase:firebase-storage-ktx") // add this
    //implementation("com.google.firebase:firebase-storage-ktx:22.0.0")

    implementation("com.google.firebase:firebase-analytics-ktx") // optional

    // Optional Realtime DB
    // implementation(libs.firebase.database)

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // Accompanist
    implementation("com.google.accompanist:accompanist-flowlayout:0.36.0")

    // Razorpay
    implementation("com.razorpay:checkout:1.6.33")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation(libs.androidx.runtime)
    implementation(libs.material3)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.foundation.layout)
    kapt("com.google.dagger:hilt-compiler:2.51.1")

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // Coroutines Play Services
    implementation(libs.kotlinx.coroutines.play.services)

    // AndroidX Core & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

//
//dependencies {
//
//    implementation("com.google.accompanist:accompanist-flowlayout:0.36.0")
//    // Retrofit
//    implementation("com.squareup.retrofit2:retrofit:2.11.0")
//// Converter (Gson for JSON)
//    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
//// OkHttp (for networking)
//    implementation("com.squareup.okhttp3:okhttp:4.12.0")
//    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
//// Coroutines support
//
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
//    implementation("com.razorpay:checkout:1.6.33")
//    // Hilt core
//    implementation ("com.google.dagger:hilt-android:2.51.1")
//    implementation(libs.androidx.navigation.common.android)
//    implementation(libs.billing)
//    kapt ("com.google.dagger:hilt-compiler:2.51.1")
//
//    // (Optional) Hilt + Navigation Compose
//    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")
//    implementation(platform("com.google.firebase:firebase-bom:34.1.0"))
//    implementation("com.google.firebase:firebase-analytics")
//    implementation("com.google.firebase:firebase-auth")
//// Core navigation for Compose
//    implementation("androidx.navigation:navigation-compose:2.8.0")
//// (Optional) If you use ViewModels in navigation destinations
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
//// (Optional) If you use Hilt with Compose Navigation
//    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
////   implementation "androidx.lifecycle:lifecycle-viewmodel-compose"
////    implementation "androidx.lifecycle:lifecycle-runtime-ktx"
////    implementation "androidx.compose.material3:material3"
//    implementation(platform(libs.firebase.bom))
//    implementation(libs.firebase.firestore)
//    // (optional Realtime DB)
//    // implementation(libs.firebase.database)
//
//    // Room
//    implementation(libs.room.runtime)
//    implementation(libs.room.ktx)
//    kapt(libs.room.compiler)
//
//    // Coroutines
//    implementation(libs.kotlinx.coroutines.play.services)
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.lifecycle.runtime.ktx)
//    implementation(libs.androidx.activity.compose)
//    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.androidx.ui)
//    implementation(libs.androidx.ui.graphics)
//    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(platform(libs.androidx.compose.bom))
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)
//}