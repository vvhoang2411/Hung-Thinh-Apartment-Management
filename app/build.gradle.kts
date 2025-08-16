plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.hungthinhapartmentmanagement"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.hungthinhapartmentmanagement"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.appcompat:appcompat:1.6.1")
// Hoặc phiên bản mới hơn
    implementation("com.google.android.material:material:1.12.0")
// Hoặc phiên bản mới hơn
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
// Hoặc phiên bản mới hơn
    implementation("androidx.recyclerview:recyclerview:1.3.2")
// Quan trọng cho RecyclerView
    implementation("androidx.cardview:cardview:1.0.0")
// Quan trọng cho CardView
}