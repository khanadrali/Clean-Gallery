

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.avrioc.cleangallery"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.avrioc.cleangallery"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
            )
        )
    }

    buildFeatures {
        dataBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    // Allow references to generated code

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.fragment.ktx)


    //lib for Viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)


    // lib for Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // lib for Hilt
    implementation(libs.hilt.android.v248)
    implementation(libs.androidx.junit.ktx)
    kapt(libs.hilt.compiler)

    // lib for Glide for Loading and cashing images
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    // lib for navigation component
    implementation(libs.androidx.navigation.fragment.ktx.v277)
    implementation(libs.androidx.navigation.ui.ktx.v277)

    // lib for lottie effect
    implementation(libs.lottie)

    // lib for Material Dialog
    implementation(libs.materialdialog)

    // lib for pagination
    implementation(libs.androidx.paging.runtime.ktx)

    // lib for testing
    testImplementation(libs.kotlinx.coroutines.test.v171)
    testImplementation(libs.mockk) // or latest version
    androidTestImplementation(libs.mockk.android)
    testImplementation(libs.junit)
}
