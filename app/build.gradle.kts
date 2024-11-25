plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.kotlin)
  alias(libs.plugins.ksp)
  alias(libs.plugins.hilt)
  id("com.google.gms.google-services")
}

android {
  namespace = "com.openclassrooms.hexagonal.games"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.openclassrooms.hexagonal.games"
    minSdk = 24
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        signingConfig = signingConfigs.getByName("debug")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.11"
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    compose = true
  }
}

dependencies {
  //kotlin
  implementation(platform(libs.kotlin.bom))

  //DI
  implementation(libs.hilt)
  implementation(libs.firebase.auth.ktx)
  implementation(libs.firebase.storage.ktx)
  ksp(libs.hilt.compiler)
  implementation(libs.hilt.navigation.compose)

  //compose
  implementation(platform(libs.compose.bom))
  implementation(libs.compose.ui)
  implementation(libs.compose.ui.graphics)
  implementation(libs.compose.ui.tooling.preview)
  implementation(libs.material)
  implementation(libs.compose.material3)
  implementation(libs.lifecycle.runtime.compose)
  debugImplementation(libs.compose.ui.tooling)
  debugImplementation(libs.compose.ui.test.manifest)
  implementation(libs.activity.compose)
  implementation(libs.navigation.compose)

  //Coroutines
  implementation(libs.kotlinx.coroutines.android)

  //Coil
  implementation(libs.coil.compose)
  implementation(libs.accompanist.permissions)


  //Pager
  implementation (libs.accompanist.pager)
  implementation (libs.accompanist.pager.indicators)


  //Core-ktx
  implementation ("androidx.core:core-ktx:1.12.0")
  //DataStore to persistence of  notification preference of the user
  implementation (libs.datastore.preferences)

  // --------------- [FireBase Stuff]
  implementation(platform(libs.firebase.bom))
  //- Analytics
  implementation(libs.firebase.analytics)
  //- Authentication
  implementation (libs.firebase.auth)
  //- UI Authentication
  implementation (libs.firebase.ui.auth)
  //- Messaging
  implementation (libs.firebase.messaging)
  // - FireStore
  implementation (libs.firebase.firestore)



  //----------------- Tests

  testImplementation(libs.junit)
  androidTestImplementation(libs.ext.junit)
  androidTestImplementation(libs.espresso.core)

  testImplementation (libs.kotlinx.coroutines.test)
  testImplementation (libs.mockk)


  testImplementation (libs.mockito.inline)
  testImplementation (libs.mockito.core)
  testImplementation (libs.mockito.kotlin)

}