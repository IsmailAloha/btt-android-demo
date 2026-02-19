plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.bluetriangle.macrobenchmark"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        targetSdk = 36

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    }

    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It"s signed with a debug key
        // for easy local/CI testing.
        create("benchmark") {
            isDebuggable = true
            signingConfig = getByName("debug").signingConfig
            matchingFallbacks += listOf("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    flavorDimensions += listOf("version")
    productFlavors {
        create("compose") { dimension = "version" }
        create("layout") { dimension = "version" }
    }

    targetProjectPath = ":app"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation(libs.androidx.junit)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.uiautomator)
    implementation(libs.androidx.benchmark.macro.junit4)
    implementation(libs.fork.btt.android.sdk) {
        exclude("com.squareup.okhttp3")
    }
    // Mockito framework
    implementation("org.mockito:mockito-android:5.13.0")
    // mockito-kotlin
    implementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
}

androidComponents {
    beforeVariants(selector().all()) {
        it.enable = it.buildType == "benchmark"
    }
}