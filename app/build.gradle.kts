plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
 //   alias(libs.plugins.devtoolsKsp)
    alias(libs.plugins.googleDaggerHilt)
    id ("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)

    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.firestorage"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.firestorage"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }


    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //Firebase
    implementation(libs.firebase.bom)
    implementation(libs.firebase.storage)


    //Dagger Hilt
    implementation(libs.com.google.dagger)
    //  ksp(libs.com.google.dagger.compiler)
    kapt(libs.com.google.dagger.compiler)
    implementation(libs.com.google.dagger.navigation)



    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    //Compose
    implementation (platform(libs.androidx.compose.bom))
    implementation (libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation (libs.androidx.runtime)
    implementation (libs.androidx.ui.graphics)
    implementation (libs.androidx.ui.tooling.preview)
    implementation (libs.androidx.activity.compose)
    implementation(libs.coil.compose)

}