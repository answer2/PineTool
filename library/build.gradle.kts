
plugins {
    id("com.android.library")
    
}

android {
    namespace = "com.answer.pina"
    compileSdk = 33
    defaultConfig {
        minSdk = 19
        targetSdk = 33
        
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildTypes {
    release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    
}

dependencies {
    implementation("top.canyie.pine:core:0.3.0")
}
