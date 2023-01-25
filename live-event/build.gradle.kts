import com.pubnub.components.buildsrc.Libs

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    namespace = "com.pubnub.components.example.live_event"
    compileSdk = Libs.Build.Android.compileSdk

    defaultConfig {
        applicationId = "com.pubnub.components.example.live_event"
        minSdk = Libs.Build.Android.minSdk
        targetSdk = Libs.Build.Android.targetSdk
        versionCode = Libs.Build.Android.versionCode
        versionName = Libs.Build.Android.versionName

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
        buildConfigField("String", "YOUTUBE_KEY", project.property("YOUTUBE_KEY") as String)
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
        jvmTarget = Libs.Build.Kotlin.jvmTarget
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Libs.AndroidX.Compose.compilerVersion
    }
}

dependencies {
    implementation(fileTree("libs") { include("*.jar") })
    implementation(Libs.PubNub.Components.chatTest)
    api(platform(Libs.PubNub.bom))
    implementation(Libs.PubNub.kotlin)
    implementation(Libs.PubNub.memberships)

    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.fragment)
    implementation(platform(Libs.AndroidX.Compose.bom))
    implementation(Libs.AndroidX.Compose.ui)
    implementation(Libs.AndroidX.Compose.material)
    implementation(Libs.AndroidX.Compose.toolingPreview)
    implementation(Libs.AndroidX.Lifecycle.runtime)
    implementation(Libs.AndroidX.Activity.activityCompose)
    implementation(Libs.Coil.coil)
    implementation(Libs.JakeWharton.timber)
    implementation(Libs.Accompanist.navigation)
    implementation(Libs.Accompanist.placeholder)
    implementation(Libs.Github.faker)

    testImplementation(Libs.JUnit.junit)
    androidTestImplementation(platform(Libs.AndroidX.Compose.bom))
    androidTestImplementation(Libs.AndroidX.Test.Ext.junit)
    androidTestImplementation(Libs.AndroidX.Test.espressoCore)
    androidTestImplementation(Libs.AndroidX.Compose.uiTest)
    debugImplementation(Libs.AndroidX.Compose.tooling)


    releaseImplementation(Libs.Utils.taktNoOp)
    debugImplementation(Libs.Utils.takt)
}
