import java.io.FileInputStream
import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}
// Load secrets from secrets.properties
val secretsPropsFile = rootProject.file("secrets.properties")
val secrets = Properties()
if (secretsPropsFile.exists()) {
    secrets.load(FileInputStream(secretsPropsFile))
} else {
    throw GradleException("Secrets file not found at: ${secretsPropsFile.absolutePath}")
}
android {
    namespace = "com.example.recipesearch"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.recipesearch"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        // Inject the API key and ID into BuildConfig
        buildConfigField("String", "EDAMAM_API_KEY", "\"${getApiKey()}\"")
        buildConfigField("String", "EDAMAM_API_ID", "\"${getApiId()}\"")

        // Debug prints (optional) to verify key loading
        manifestPlaceholders["EDAMAM_API_KEY"] = getApiKey()
        manifestPlaceholders["EDAMAM_API_ID"] = getApiId()
        println("EDAMAM_API_KEY = ${System.getenv("EDAMAM_API_KEY") ?: secrets.getProperty("EDAMAM_API_KEY")}")
        println("EDAMAM_API_ID = ${System.getenv("EDAMAM_API_ID") ?: secrets.getProperty("EDAMAM_API_ID")}")
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}
// Function to retrieve the API key from secrets.properties or environment variables
fun getApiKey(): String {
    val secretsKey = secrets.getProperty("EDAMAM_API_KEY")
    if (!secretsKey.isNullOrEmpty()) {
        return secretsKey
    }
    val envKey = System.getenv("EDAMAM_API_KEY")
    if (!envKey.isNullOrEmpty()) {
        return envKey
    }
    throw GradleException("API Key not found! Please define EDAMAM_API_KEY in secrets.properties or as an environment variable.")
}

// Function to retrieve the API ID from secrets.properties or environment variables
fun getApiId(): String {
    val secretsId = secrets.getProperty("EDAMAM_API_ID")
    if (!secretsId.isNullOrEmpty()) {
        return secretsId
    }
    val envId = System.getenv("EDAMAM_API_ID")
    if (!envId.isNullOrEmpty()) {
        return envId
    }
    throw GradleException("API ID not found! Please define EDAMAM_API_ID in secrets.properties or as an environment variable.")
}

dependencies {
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // Jetpack Compose Google Maps binding
    implementation("com.google.maps.android:maps-compose:2.11.4")

    // Coil for image loading (optional if you're using it)
    implementation("io.coil-kt:coil-compose:2.2.2")
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
}