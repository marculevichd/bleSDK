plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.testsdkble"
    compileSdk = 34

    sourceSets {
        getByName("main") {
//            jniLibs.srcDirs("src/main/jniLibs")
            jniLibs.srcDirs("jniLibs")
//            jniLibs.srcDirs ("libs")
        }
    }

    defaultConfig {
        applicationId = "com.example.testsdkble"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(files("libs/IDoBLELib-metaSDk-1.00.09.jar"))
    implementation("com.google.code.gson:gson:2.8.8")


    // Timber для логирования
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Основная зависимость Hyperion
    debugImplementation("com.willowtreeapps.hyperion:hyperion-core:0.9.38")

    // Плагин для работы с логами в Hyperion
    debugImplementation("com.willowtreeapps.hyperion:hyperion-timber:0.9.38")


//    implementation ("androidx.core:core-ktx:1.10.1")
//    implementation ("androidx.activity:activity-compose:1.8.0")

}