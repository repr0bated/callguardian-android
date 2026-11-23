import net.pwall.json.schema.codegen.CodeGenerator

buildscript {
    repositories { mavenCentral() }
    dependencies { classpath("net.pwall.json:json-kotlin-schema-codegen:0.123") }
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

// google-services.json is optional. Drop the file from the Firebase console into app/
// and the Google Services plugin is applied; otherwise the app falls back to the
// FIREBASE_* values below (gradle properties / env), or to placeholders so it still builds.
val hasGoogleServicesJson = file("google-services.json").exists()
if (hasGoogleServicesJson) {
    apply(plugin = libs.plugins.gms.get().pluginId)
}

fun cfg(name: String, default: String = ""): String =
    (project.findProperty(name) as String?) ?: System.getenv(name) ?: default

// Release signing: keystore lives OUTSIDE the repo. Point to it from
// ~/.gradle/gradle.properties or env: CG_KEYSTORE, CG_KEYSTORE_PASSWORD, CG_KEY_ALIAS, CG_KEY_PASSWORD
val releaseKeystore = cfg("CG_KEYSTORE").takeIf { it.isNotBlank() }?.let { file(it) }

android {
    namespace = "com.example.callguardian"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.github.repr0bated.callguardian"
        minSdk = 29
        targetSdk = 36
        versionCode = (System.getenv("GITHUB_RUN_NUMBER")?.toIntOrNull() ?: 0) + 20
        versionName = "2.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }

        buildConfigField("boolean", "HAS_GOOGLE_SERVICES_JSON", hasGoogleServicesJson.toString())
        buildConfigField("String", "FIREBASE_API_KEY", "\"${cfg("FIREBASE_API_KEY")}\"")
        buildConfigField("String", "FIREBASE_APP_ID", "\"${cfg("FIREBASE_APP_ID")}\"")
        buildConfigField("String", "FIREBASE_PROJECT_ID", "\"${cfg("FIREBASE_PROJECT_ID")}\"")
        buildConfigField("String", "CLOUD_MODEL", "\"${cfg("CLOUD_MODEL", "gemini-3.5-flash-lite")}\"")
    }

    signingConfigs {
        if (releaseKeystore != null && releaseKeystore.exists()) {
            create("release") {
                storeFile = releaseKeystore
                storePassword = cfg("CG_KEYSTORE_PASSWORD")
                keyAlias = cfg("CG_KEY_ALIAS", "callguardian")
                keyPassword = cfg("CG_KEY_PASSWORD", cfg("CG_KEYSTORE_PASSWORD"))
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.findByName("release")
        }
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "/META-INF/INDEX.LIST",
                "/META-INF/DEPENDENCIES",
                "/META-INF/io.netty.versions.properties",
            )
        }
    }

    testOptions { unitTests.isReturnDefaultValues = true }
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        optIn.add("com.google.firebase.ai.type.PublicPreviewAPI")
    }
}

// ---------------------------------------------------------------------------------------------
// Typed methods: Kotlin types are GENERATED from the JSON Schemas in src/main/assets/schemas
// (the same files the app validates against at runtime), using json-kotlin-schema-codegen.
// ---------------------------------------------------------------------------------------------
val schemaDir = layout.projectDirectory.dir("src/main/assets/schemas")
val generatedSchemaDir = layout.buildDirectory.dir("generated/source/schemas/kotlin")
val generateSchemaTypes by tasks.registering {
    description = "Generates Kotlin data classes from the method registry JSON Schemas"
    inputs.dir(schemaDir)
    inputs.file("schema-codegen.json")
    outputs.dir(generatedSchemaDir)
    doLast {
        val out = generatedSchemaDir.get().asFile
        out.deleteRecursively()
        val files = listOf(schemaDir.file("registry.schema.json").asFile) +
            schemaDir.dir("methods").asFile.listFiles()!!.filter { it.name.endsWith(".json") }.sortedBy { it.name }
        CodeGenerator().apply {
            configure(file("schema-codegen.json"))
            baseDirectoryName = out.absolutePath
            basePackageName = "com.example.callguardian.methods.gen"
        }.generate(files)
    }
}
android.sourceSets.getByName("main").java.srcDir(generatedSchemaDir)
tasks.matching { it.name.startsWith("compile") && it.name.endsWith("Kotlin") || it.name.startsWith("ksp") }
    .configureEach { dependsOn(generateSchemaTypes) }

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.datastore.preferences)

    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi.kotlin)

    // Firebase AI Logic: cloud Gemini + on-device (ML Kit Prompt API / AICore) hybrid
    implementation(libs.firebase.common)
    implementation(libs.firebase.ai)
    implementation(libs.firebase.ai.ondevice)
    ksp(libs.firebase.ai.ksp)
    implementation(libs.firebase.appcheck.playintegrity)
    debugImplementation(libs.firebase.appcheck.debug)

    implementation(libs.json.schema.validator)
    implementation(libs.timber)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
