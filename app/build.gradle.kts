plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.spotify"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.spotify"
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.palette.ktx)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation("com.google.accompanist:accompanist-permissions:0.36.0")

    //room
    val room_version = "2.8.4"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:${room_version}")
    implementation("androidx.room:room-paging:${room_version}")

    //dagger
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //MediaSession
    implementation("androidx.media:media:1.7.0")
    implementation("androidx.media3:media3-exoplayer:1.9.2")

    //coil (compatible with Kotlin 2.0.x)
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("io.coil-kt:coil:2.7.0")

    //Material icons extended
    implementation("androidx.compose.material:material-icons-extended")

    //Material3
    implementation("androidx.navigation3:navigation3-runtime:1.1.0-rc01")
    implementation("androidx.navigation3:navigation3-ui:1.1.0-rc01")



}
