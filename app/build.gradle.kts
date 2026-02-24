import org.gradle.kotlin.dsl.androidTestImplementation
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.daggerHiltAndroid)
    alias(libs.plugins.safeargs.kotlin)
    id("kotlin-android")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("fullstory")
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.compose)
    id("com.google.firebase.crashlytics")
}

fullstory {
    org = "o-23ZG0X-na1"
    enabledVariants = "all"
    logLevel = "debug"
    logcatLevel = "debug"
}
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if(keystorePropertiesFile.exists()) {
        load(FileInputStream(keystorePropertiesFile))
    }
}

android {
    namespace = "com.bluetriangle.bluetriangledemo"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.bluetriangle.bluetriangledemo"
        minSdk = 23
        targetSdk = 36
        versionCode = 32
        versionName = "2.19.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    androidComponents {
        beforeVariants { variant ->
            if (variant.buildType?.contains("Debug", ignoreCase = true) == true) {
                println(variant.name)
                variant.enableAndroidTest = false
            }
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
    flavorDimensions += listOf("framework", "site")
    productFlavors {
        // framework dimension
        create("compose") {
            dimension = "framework"
            applicationIdSuffix = ".compose"
            resValue("string", "app_name", "eCom Compose")
            manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher_compose"
            manifestPlaceholders["appIconRound"] = "@mipmap/ic_launcher_compose_round"
        }
        create("layout") {
            dimension = "framework"
            applicationIdSuffix = ".layout"
            resValue("string", "app_name", "eCom Layout")
            manifestPlaceholders["appIcon"] = "@mipmap/ic_launcher_layout"
            manifestPlaceholders["appIconRound"] = "@mipmap/ic_launcher_layout_round"
        }
        // Site dimension
        create("demo") {
            dimension = "site"
            buildConfigField("String", "SITE_ID", "\"sdkdemo26621z\"")
        }

        create("devTest") {
            dimension = "site"
            buildConfigField("String", "SITE_ID", "\"sdkdevtest500z\"")
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        compose = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
}

dependencies {
    implementation(libs.btt.android.sdk) {
        exclude("com.squareup.okhttp3", "okhttp-bom")
    }
    implementation("com.microsoft.clarity:clarity:3.1.3")
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    implementation(platform(libs.firebase.bom))

    // Add the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)


    implementation(libs.gson)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    implementation(libs.hilt.android)
    implementation(libs.activity.ktx)
    implementation(libs.androidx.activity)
    ksp(libs.hilt.compiler)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    implementation(libs.retrofit2.retrofit)
    implementation(libs.converter.moshi)

    implementation(libs.glide)
    ksp(libs.glide.ksp)
    implementation(libs.glide.compose)
    implementation(libs.okhttp3.integration)

    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    // Jetpack Compose Dependencies
    implementation(platform(libs.compose.bom))
    implementation(libs.activity.compose)
    implementation(libs.runtime.livedata)

    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material.android)
    implementation(libs.fragment.ktx)
    implementation(libs.navigation.compose)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.speed.dial)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.core)

}
