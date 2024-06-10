import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlinParcelize)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.appPackageName.get()
        namespace = libs.versions.appPackageName.get()
        minSdk = libs.versions.androidMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
        versionCode = libs.versions.appVersion.get().toInt()
        versionName = libs.versions.appVersionName.get()
        archivesName = "everyweather"
        setProperty("archivesBaseName", archivesName.get() + "_v" + versionName)
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    val signConfigName = "release_sign_config"
    signingConfigs {
        val properties = getLocalProperties()
        val alias = properties.getPropertyValue("KEY_ALIAS")
        val keystorePassword = properties.getPropertyValue("KEYSTORE_PASSWORD")
        create(signConfigName) {
            storeFile = file("../sign/everyweather_keystore.jks")
            keyAlias = alias
            storePassword = keystorePassword
            keyPassword = keystorePassword
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.findByName(signConfigName)
        }
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.findByName(signConfigName)
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    //Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    //Compose
    implementation(libs.bundles.compose)

    //Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.compose.navigation)

    //Koin DI
    implementation(libs.bundles.koin)

    //Network
    implementation(libs.bundles.network)

    //Room
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    //Datastore
    implementation(libs.datastore.preferences)

    //Analytic
    implementation(libs.bundles.analytic)

    //Google Services
    implementation(libs.bundles.google.services)

    //Util
    implementation(libs.app.update)

    //Пришлось добавить зависимость lifecycle т.к appCompat 1.5.1 без неё не работает
    implementation(libs.android.viewmodel)

    //Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}

fun Project.getLocalProperties(): Properties {
    val localPropertiesFileName = "local.properties"
    val properties = Properties()
    val localProperties = File(localPropertiesFileName)
    if (localProperties.isFile) {
        InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    } else error("File \"$localPropertiesFileName\" not found")

    return properties
}

fun Properties.getPropertyValue(key: String): String {
    return this.getProperty(key) as String
}