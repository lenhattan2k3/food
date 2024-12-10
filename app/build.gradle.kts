plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.apporder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.apporder"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    // Enable View Binding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Firebase BOM (Bill of Materials) for syncing Firebase SDK versions
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    // Firebase dependencies for Realtime Database, Authentication, and Storage
    implementation("com.google.firebase:firebase-database-ktx")   // Firebase Realtime Database
    implementation("com.google.firebase:firebase-auth-ktx")      // Firebase Authentication
    implementation("com.google.firebase:firebase-storage-ktx")   // Firebase Storage
    implementation("com.google.firebase:firebase-firestore-ktx:24.4.0") // Firebase Firestore

    // RecyclerView dependency
    implementation("androidx.recyclerview:recyclerview:1.3.0")  // RecyclerView

    // Picasso for image loading
    implementation("com.squareup.picasso:picasso:2.71828")

    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    // Glide for image loading
    implementation ("com.github.bumptech.glide:glide:4.15.1") // Glide dependency
    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1") // Required for Glide annotation processing

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

