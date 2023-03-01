@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "com.abakan.electronics.mycountries"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.abakan.electronics.mycountries"
        minSdk = 22
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.abakan.electronics.mycountries.testing.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":ui"))
    implementation(project(":feature:countries"))
    implementation(project(":data"))
    implementation(project(":testing"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.preview)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.coroutines)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.android.compiler)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.hilt.testing)
    kaptAndroidTest(libs.dagger.hilt.android.compiler)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}