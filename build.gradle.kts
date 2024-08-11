buildscript {
    dependencies {
        classpath(libs.secrets.gradle.plugin)
        classpath(libs.google.services)
        classpath(libs.firebase.crashlyticsGradle)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.gradle)
    }
}
plugins {
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlin.ksp).apply(false)
    alias(libs.plugins.kotlinParcelize).apply(false)
    alias(libs.plugins.kotlinSerialization).apply(false)
}