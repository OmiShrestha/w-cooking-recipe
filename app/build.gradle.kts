import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    //Added for Room support
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.miomi.recipe"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.miomi.recipe"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Load sensitive data from local.properties and add them to this BuildConfig
        val localProps = Properties()
        localProps.load(rootProject.file("local.properties").inputStream())
        buildConfigField("String", "WEB_CLIENT_ID", "\"${localProps["WEB_CLIENT_ID"]}\"")
        buildConfigField("String", "ADMIN_EMAIL_1", "\"${localProps["ADMIN_EMAIL_1"]}\"")
        buildConfigField("String", "ADMIN_EMAIL_2", "\"${localProps["ADMIN_EMAIL_2"]}\"")
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    //Added for Room support
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")
    implementation("androidx.compose.material:material-icons-extended")
    ksp("androidx.room:room-compiler:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")

    //needed for testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    //Added for navigation support
    implementation("androidx.navigation:navigation-compose:2.7.7")

    //Added for image loading support
    implementation("io.coil-kt:coil-compose:2.7.0")

    //Added for network/API support
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    //Added for Google Sign-In support
    implementation(libs.credentials)
    implementation(libs.credentials.play.services)
    implementation(libs.googleid)

    //Added for font support
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.ui)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}