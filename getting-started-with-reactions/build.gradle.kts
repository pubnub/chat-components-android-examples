import com.pubnub.components.buildsrc.Libs

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.pubnub.components.example.getting_started"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "PUBLISH_KEY", project.property("PUBNUB_PUBLISH_KEY") as String)
        buildConfigField(
            "String",
            "SUBSCRIBE_KEY",
            project.property("PUBNUB_SUBSCRIBE_KEY") as String
        )
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
        kotlinCompilerExtensionVersion = Libs.AndroidX.Compose.version
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(Libs.PubNub.Components.chat)
    api(platform(Libs.PubNub.bom))
    implementation(Libs.PubNub.kotlin)
    implementation(Libs.PubNub.memberships)
    implementation(Libs.PubNub.coroutines)
    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.toolingPreview)
    implementation(Libs.AndroidX.Lifecycle.runtime)
    implementation(Libs.AndroidX.Activity.activityCompose)
    implementation(Libs.Coil.coil)
    implementation(Libs.JakeWharton.timber)

    testImplementation(Libs.JUnit.junit)
    androidTestImplementation(Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation(Libs.AndroidX.Test.espressoCore)
    androidTestImplementation(Libs.AndroidX.Compose.uiTest)
    debugImplementation(Libs.AndroidX.Compose.tooling)

    implementation(Libs.Accompanist.placeholder)
}
