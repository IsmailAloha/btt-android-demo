plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.benchmark)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.bluetriangle.benchmark"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    defaultConfig {
        minSdk = 24
        targetSdk = 34

        testInstrumentationRunner = "androidx.benchmark.junit4.AndroidBenchmarkRunner"
        testInstrumentationRunnerArguments.put("androidx.benchmark.suppressErrors", "NOT_PROFILEABLE,NOT_GABLE,EMULATOR,LOW_BATTERY,PACKAGE_NOT_INSTALLED,GRANT_PERMISSION_ERROR")
    }

    testBuildType = "release"
    buildTypes {
        debug {
            // Since isDebuggable can"t be modified by gradle for library modules,
            // it must be done in a manifest - see src/androidTest/AndroidManifest.xml
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "benchmark-proguard-rules.pro"
            )
        }
        release {
            isDefault = true
        }
    }
}

dependencies {
    implementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.benchmark.junit4)
    androidTestImplementation(libs.clarity)
    androidTestImplementation(libs.btt.android.sdk) {
        exclude("com.squareup.okhttp3")
    }
    // Mockito framework
    androidTestImplementation("org.mockito:mockito-android:5.13.0")
    // mockito-kotlin
    androidTestImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    // Add your dependencies here. Note that you cannot benchmark code
    // in an app module this way - you will need to move any code you
    // want to benchmark to a library module:
    // https://developer.android.com/studio/projects/android-library#Convert

}