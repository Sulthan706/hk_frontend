plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-android")
//    kotlin("android.extensions")
}

android {
    namespace = "com.hkapps.academy"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures {
        viewBinding = true
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")
    implementation("com.google.firebase:firebase-messaging-ktx:23.3.1")
    implementation("com.google.android.gms:play-services-fido:20.1.0")
    implementation("com.google.firebase:protolite-well-known-types:18.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    implementation("androidx.activity:activity:1.8.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation("com.google.truth:truth:1.1.3")
    androidTestImplementation("com.google.truth:truth:1.1.3")
    implementation("com.github.Cielsk:clearable-edittext:0.0.5")
//    compileOnly("com.github.pengrad:jdk9-deps:1.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("io.reactivex.rxjava2:rxjava:2.2.6")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("android.arch.lifecycle:extensions:1.1.1")

    implementation("com.google.dagger:dagger:2.28.3")
    implementation("com.google.dagger:dagger-android-support:2.14.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    kapt("com.google.dagger:dagger-compiler:2.14.1")
    kapt("com.google.dagger:dagger-android-processor:2.14.1")
    implementation("com.pixplicity.easyprefs:EasyPrefs:1.10.0")

    //RESPONSIVE LIB
    //dp
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    //sp
    implementation("com.intuit.ssp:ssp-android:1.0.6")

    // date month
//    implementation("com.dibyendu.picker:picker:1.0.0")

    //lottie
    implementation("com.airbnb.android:lottie:3.7.0")
    implementation("com.faltenreich:skeletonlayout:4.0.0")
    implementation("com.github.realpacific:click-shrink-effect:2.0")

    //CAMERA
    implementation("androidx.activity:activity-ktx:1.2.3")
    implementation("androidx.fragment:fragment-ktx:1.3.3")

    //SHIMMER EFFECT
    implementation("com.facebook.shimmer:shimmer:0.1.0@aar")

    // expandable
    implementation("com.github.cachapa:ExpandableLayout:2.9.2")
    implementation("com.github.skydoves:expandablelayout:1.0.7")

    // spinner
    implementation("com.github.chivorns:smartmaterialspinner:1.5.0")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:26.5.0"))
    implementation("com.google.firebase:firebase-config:20.0.4")
    implementation("com.google.firebase:firebase-analytics:18.0.3")
    implementation("com.google.firebase:firebase-core:18.0.3")
    implementation("com.google.firebase:firebase-auth:16.0.5")
    implementation("com.google.firebase:firebase-database:16.0.5")
    implementation("com.google.firebase:firebase-storage:16.0.5")

    //push notif
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.firebase:firebase-client-android:2.3.1")

    //image round circular imageview
//    implementation("com.mikhaellopez:circularimageview:3.2.0")


    //Glide
    implementation("com.github.bumptech.glide:glide:4.7.1")
    implementation("com.github.bumptech.glide:annotations:4.7.1")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.0.0")

    //Custom Calendar (ribet)
    implementation("com.github.sundeepk:compact-calendar-view:3.0.0")

    // pdf viewer
//    implementation("com.github.barteksc:android-pdf-viewer:3.1.0-beta.1")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    implementation("de.hdodenhof:circleimageview:3.1.0")
}