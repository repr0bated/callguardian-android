import java.io.File

plugins {
    alias(libs.plugins.agp.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.gms)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.example.callguardian"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.callguardian"
        minSdk = 29
        targetSdk = 35
        versionCode = System.getenv("GITHUB_RUN_NUMBER")?.toInt() ?: 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    signingConfigs {
        create("release") {
            storeFile = file("keystore.jks")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(platform(libs.libraries.compose.bom))
    implementation(platform(libs.libraries.firebase.bom))
    implementation(libs.libraries.firebase.vertexai)
    implementation(libs.libraries.androidx.core.ktx)
    implementation(libs.libraries.androidx.appcompat)
    implementation(libs.libraries.androidx.lifecycle.runtime.ktx)
    implementation(libs.libraries.androidx.lifecycle.runtime.compose)
    implementation(libs.libraries.androidx.activity.compose)
    implementation(libs.libraries.androidx.compose.ui)
    implementation(libs.libraries.androidx.compose.ui.tooling.preview)
    implementation(libs.libraries.androidx.compose.material3)
    implementation(libs.libraries.androidx.compose.material)
    implementation(libs.libraries.androidx.navigation.compose)
    implementation(libs.libraries.androidx.hilt.navigation.compose)
    implementation(libs.libraries.hilt.android)
    kapt(libs.libraries.hilt.compiler)

    implementation(libs.libraries.kotlinx.coroutines.core)
    implementation(libs.libraries.kotlinx.coroutines.android)

    implementation(libs.libraries.androidx.room.runtime)
    implementation(libs.libraries.androidx.room.ktx)
    kapt(libs.libraries.androidx.room.compiler)

    implementation(libs.libraries.androidx.work.runtime.ktx)
    implementation(libs.libraries.androidx.datastore.preferences)

    implementation(libs.libraries.retrofit)
    implementation(libs.libraries.retrofit.converter.moshi)
    implementation(libs.libraries.moshi.kotlin)
    kapt(libs.libraries.moshi.kotlin.codegen)
    implementation(libs.libraries.okhttp.logging.interceptor)

    implementation(libs.libraries.timber)

    testImplementation(libs.libraries.junit)
    androidTestImplementation(libs.libraries.androidx.test.ext.junit)
    androidTestImplementation(libs.libraries.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.libraries.compose.bom))
    androidTestImplementation(libs.libraries.androidx.compose.ui.test.junit4)
    debugImplementation(libs.libraries.androidx.compose.ui.tooling)
    debugImplementation(libs.libraries.androidx.compose.ui.test.manifest)
}
