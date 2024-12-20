plugins {

    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.ugb.controlesbasicos"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ugb.controlesbasicos"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))

    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.firebaseui:firebase-ui-storage:8.0.2")
    implementation("com.google.firebase:firebase-auth:23.0.0")



    //elegir ciudad
    implementation("com.hbb20:ccp:2.5.1")

    implementation("com.googlecode.libphonenumber:libphonenumber:8.12.34")

    //para generar el pdf
    implementation("com.itextpdf:itext7-core:7.1.15")

    //mapa gps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.android.volley:volley:1.2.1")

    //sensor foco


}
