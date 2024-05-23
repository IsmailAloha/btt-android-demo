// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply(false)
    alias(libs.plugins.jetbrainsKotlinAndroid) apply(false)
    alias(libs.plugins.safeargs.kotlin) apply(false)
    alias(libs.plugins.daggerHiltAndroid) apply(false)
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}