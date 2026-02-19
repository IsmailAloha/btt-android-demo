// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply(false)
    alias(libs.plugins.jetbrainsKotlinAndroid) apply(false)
    alias(libs.plugins.safeargs.kotlin) apply(false)
    alias(libs.plugins.daggerHiltAndroid) apply(false)
    id("com.google.devtools.ksp") version "2.1.21-2.0.2" apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.benchmark) apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
    id("com.google.firebase.crashlytics") version "3.0.6" apply false
    alias(libs.plugins.android.test) apply false
}

buildscript {
    dependencies {
        classpath(libs.gradle.plugin.local)
    }
}