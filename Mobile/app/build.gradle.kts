import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")

    kotlin("plugin.serialization") version "2.0.21"
}



android {
    namespace = "site.smartenglish"
    compileSdk = 35

    defaultConfig {
        applicationId = "site.smartenglish"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        localProperties.load(FileInputStream(rootProject.file("local.properties")))
        buildConfigField(
            "String",
            "IP",
            "\"${localProperties.getProperty("IP")}\""
        )
        buildConfigField(
            "String",
            "SecretId",
            "\"${localProperties.getProperty("SecretId")}\""
        )
        buildConfigField(
            "String",
            "SecretKey",
            "\"${localProperties.getProperty("SecretKey")}\""
        )

    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.ui)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.ui.android.stubs)
    implementation(libs.androidx.ui.test.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //nav
    implementation(libs.androidx.navigation.compose)
    //coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    //okhttp
    implementation(libs.okhttp)
    //协程
    implementation(libs.kotlinx.coroutines.android)
    //datastore
    implementation(libs.androidx.datastore.preferences)
    //序列化
    implementation(libs.androidx.navigation.compose)
    //Splash Screen
    implementation(libs.androidx.core.splashscreen)
    //ICON
    implementation(libs.androidx.material.icons.extended)
    //JSoup
    implementation(libs.jsoup)
    // 腾讯云对象存储 SDK
    implementation(libs.qcloud.cos.android)
    // test
    // Mockk 测试库
    testImplementation (libs.mockk)
    androidTestImplementation( libs.mockk.android)
    // Compose 测试库
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.test.manifest)



}