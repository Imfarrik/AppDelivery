plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.ccinc.test"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ccinc.test"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {

            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"https://anika1d.github.io/WorkTestServer/\""
            )

        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"https://anika1d.github.io/WorkTestServer/\""
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
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

    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.splashscreen)

    debugImplementation(libs.retrofit.logger)

    implementation(project(":data"))
    implementation(project(":api"))
    implementation(project(":database"))
    implementation(project(":common"))
    implementation(project(":ui"))
    implementation(project(":design-system"))

    implementation(fileTree(mapOf(
        "dir" to "libs",
        "include" to listOf("*.aar", "*.jar"),
    )))

    implementation(libs.retrofit)
    implementation(libs.retrofit.serialization.converter)

    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.6.2")
    annotationProcessor ("androidx.lifecycle:lifecycle-compiler:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-service:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-process:2.7.0")
    implementation ("org.bouncycastle:bcpg-jdk15on:1.69")
    api ("com.fasterxml.jackson.core:jackson-databind:2.13.5")
    implementation ("com.android.volley:volley:1.2.1")
    api ("cz.msebera.android:httpclient:4.5.3")
}